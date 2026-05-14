import { useEffect, useRef, useState, type KeyboardEvent } from "react";
import { streamAnswer, type DocItem } from "../services/documentService";
import { useToast } from "../context/ToastContext";
import MessageBubble from "./MessageBubble";
import "./ChatWindow.css";

interface Message {
  id: string;
  role: "user" | "assistant";
  content: string;
  streaming?: boolean;
}

interface Props {
  doc: DocItem;
}

export default function ChatWindow({ doc }: Props) {
  const { toast } = useToast();
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [busy, setBusy] = useState(false);
  const scrollRef = useRef<HTMLDivElement>(null);

  // Reset on doc change
  useEffect(() => {
    setMessages([]);
    setInput("");
  }, [doc.id]);

  useEffect(() => {
    scrollRef.current?.scrollTo({ top: scrollRef.current.scrollHeight, behavior: "smooth" });
  }, [messages]);

  const send = async () => {
    const question = input.trim();
    if (!question || busy) return;

    const userMsg: Message = { id: `u-${Date.now()}`, role: "user", content: question };
    const aiId = `a-${Date.now()}`;
    const aiMsg: Message = { id: aiId, role: "assistant", content: "", streaming: true };
    setMessages((prev) => [...prev, userMsg, aiMsg]);
    setInput("");
    setBusy(true);

    try {
  await streamAnswer(doc.id, question, (chunk) => {

    console.log(JSON.stringify(chunk));

    setMessages((prev) =>
  prev.map((m) =>
    m.id === aiId
      ? {
          ...m,
          content:
            m.content +
            (
              m.content &&
              !m.content.endsWith(" ") &&
              !chunk.startsWith(" ")
                ? " "
                : ""
            ) +
            chunk,
        }
      : m
  )
);

  });
} catch (err: unknown) {

  const message =
    err instanceof Error
      ? err.message
      : "Something went wrong";

  toast(message, "error");

  setMessages((prev) =>
    prev.map((m) =>
      m.id === aiId
        ? {
            ...m,
            content:
              m.content ||
              "Failed to fetch answer.",
          }
        : m
    )
  );

} finally {

  setMessages((prev) =>
    prev.map((m) =>
      m.id === aiId
        ? { ...m, streaming: false }
        : m
    )
  );

  setBusy(false);
}
  };

  const onKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      send();
    }
  };

  return (
    <div className="chat">
      <div className="chat-header">
        <div>
          <div className="chat-eyebrow">Chatting with</div>
          <h2 className="chat-title">{doc.name}</h2>
        </div>
      </div>

      <div className="chat-scroll" ref={scrollRef}>
        {messages.length === 0 ? (
          <div className="chat-empty">
            <div className="chat-empty-icon">💬</div>
            <h3>Ask anything about this document</h3>
            <p>Try “Summarize the key concepts” or “Explain chapter 2 in simple terms.”</p>
          </div>
        ) : (
          messages.map((m) => (
            <MessageBubble
              key={m.id}
              role={m.role}
              content={m.content}
              streaming={m.streaming && m.content.length === 0}
            />
          ))
        )}
      </div>

      <div className="chat-input-wrap">
        <textarea
          className="chat-input"
          placeholder="Ask a question…"
          value={input}
          rows={1}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={onKeyDown}
          disabled={busy}
        />
        <button className="btn btn-primary chat-send" onClick={send} disabled={busy || !input.trim()}>
          {busy ? <span className="spinner" /> : "Send"}
        </button>
      </div>
    </div>
  );
}