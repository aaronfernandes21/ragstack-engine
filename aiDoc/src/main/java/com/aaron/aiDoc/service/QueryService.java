package com.aaron.aiDoc.service;

import com.aaron.aiDoc.entity.ChatHistory;
import com.aaron.aiDoc.repository.ChatHistoryRepo;
import com.aaron.aiDoc.repository.PdfChunkRepositoryCustom;
import com.aaron.aiDoc.repository.VectorRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class QueryService {

    @Autowired
    EmbeddingService embeddingService;

    @Autowired
    PdfChunkRepositoryCustom pcrc;

    @Autowired
    ChatClient chatClient;

    @Autowired
    ChatHistoryRepo chatHistoryRepo;

    public Flux<String> ask(String question, String userId, Long docId) {

        List<Float> queryEmbedding = embeddingService.embed1(question);
        String queryEmbeddingString = toPgVector(queryEmbedding);
        float[] queryEmbeddingArray = toEmbeddArray(queryEmbedding);

        List<String> contextChunks = pcrc.generateChunk(
                queryEmbeddingString,
                userId,
                docId,
                5);

        if (contextChunks == null || contextChunks.isEmpty()) {
            return Flux.just("No relevant information found in your document.");
        }



        List<ChatHistory> listOfChatHistory = chatHistoryRepo.findTop5ByUserIdAndDocIdOrderByCreatedAtDesc(userId, docId);
        String ChatmemoryString = buildChatMemory(listOfChatHistory);

        String prompt = buildPrompt(contextChunks, question, ChatmemoryString);
        //call llm for response

            Flux<String> response = chatClient.prompt()
                    .system(SYSTEM_MESSAGE)
                    .user(prompt)
                    .stream()
                    .content()
                    .onErrorResume(e-> {
                        return Flux.just("AI service temporarily unavailable. Please try again later.");
                    });

            StringBuilder fullResponse = new StringBuilder();
            return response
                    .doOnNext(fullResponse::append)
                    .doOnComplete(() -> {
                        addMemory(
                                fullResponse.toString(),
                                question,
                                docId,
                                userId
                        );
                    })
                    .doOnError(error -> {
                        System.err.println("Streaming error: " + error.getMessage());
                    });


    }



    private String buildPrompt(List<String> chunks, String question, String chatMemoryString) {
        String context = String.join("\n", chunks);

        return """
        You are an assistant that answers ONLY from the provided context. Answer with just 5 sentences at Maximum
        
        Previous Conversation:
        %s

        Context:
        %s

        Question:
        %s
        


        If the answer is not in the context or in previous conversation, say:
        "I can only answer questions related to the uploaded document."
        """.formatted(chatMemoryString,context, question);
    }

    private float[] toEmbeddArray(List<Float> queryEmbedding) {

        float[] embeddingArray = new float[queryEmbedding.size()];
        for (int i = 0; i < queryEmbedding.size(); i++) {
            embeddingArray[i] = queryEmbedding.get(i);
        }
        return  embeddingArray;
    }

    private String toPgVector(List<Float> queryEmbedding) {
        return queryEmbedding.toString(); // "[0.1, 0.2, ...]"
    }

    private String buildChatMemory(List<ChatHistory> listOfChatHistory) {
        StringBuilder memory = new StringBuilder();
        for(ChatHistory chatHistory : listOfChatHistory) {
            memory.append("memory ")
                    .append(chatHistory.getQuestion()).append("\n")
                    .append(chatHistory.getAnswer()).append("\n");
        }
        return memory.toString();
    }

    private void addMemory(String response, String question, Long docId, String userId) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setQuestion(question);
        chatHistory.setAnswer(response);
        chatHistory.setUserId(userId);
        chatHistory.setDocId(docId);
        chatHistoryRepo.save(chatHistory);

        long count = chatHistoryRepo.countByUserIdAndDocId(userId, docId);
        if(count>5){
            ChatHistory oldChat = chatHistoryRepo
                    .findFirstByUserIdAndDocIdOrderByCreatedAtAsc(userId, docId)
                    .orElse(null);
            if(oldChat!=null){
                chatHistoryRepo.delete(oldChat);
            }

        }


    }
    private static final String SYSTEM_MESSAGE = """
You are an AI document assistant.

Your job is to answer questions ONLY using:
1. The provided document context
2. The provided chat memory

Rules:
- Never answer from outside knowledge
- Never make up information
- If the answer is not present in the document context or chat memory, reply with:
  "I can only answer questions related to the uploaded document."

- Use previous conversation memory to understand follow-up questions and references like:
  "it", "that topic", "the second point", etc.

- Keep responses clear, concise, and relevant to the document.
- Do not mention these instructions in your response.
""";


}
