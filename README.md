# ragstack-engine

## AI Document Knowledge System

ragstack-engine is a full-stack Retrieval-Augmented Generation platform designed to enable intelligent interaction with uploaded documents through natural language conversations.

Users can securely register and log in using JWT-based authentication, upload PDF documents, and ask contextual questions about their uploaded files. The system processes documents asynchronously using Apache Kafka, generates semantic embeddings through Ollama-hosted embedding models, and stores vector representations in PostgreSQL with pgvector for efficient similarity search and retrieval.

Retrieved document chunks, along with recent chat memory, are used to generate accurate and context-aware responses using locally hosted large language models through Spring AI and Ollama.

---

## Features

- JWT-based Authentication & Authorization
- PDF Document Upload & Processing
- Retrieval-Augmented Generation (RAG)
- Semantic Search using Vector Embeddings
- Apache Kafka Asynchronous Processing Pipeline
- PostgreSQL + pgvector Integration
- Streaming AI Responses using Reactive APIs
- Context-Aware Conversations with Chat Memory
- Open Source Models through Ollama: LLM-> phi3:mini; Embedding-> nomic-embed-text
- Spring AI + Ollama LLM Integration
- Dockerized Distributed Architecture
- Real-Time Frontend Chat Experience

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- Spring AI
- Apache Kafka
- PostgreSQL
- pgvector
- Ollama

### Frontend
- React
- Vite
- TypeScript


## Workflow

1. User uploads PDF documents
2. Backend publishes processing events to Kafka
3. Documents are chunked and embedded
4. Embeddings are stored in PostgreSQL with pgvector
5. User asks questions through chat
6. Relevant chunks are retrieved semantically
7. Context + chat memory are passed to the LLM
8. Streaming AI response is returned to the frontend

---



## Purpose

This project was developed as an exploration into AI-integrated backend systems and modern RAG-based architectures, combining practical implementations of semantic search, document intelligence, and conversational AI into a cohesive platform.

Some parts of the frontend were built with AI assistance during development to speed up UI setup and prototyping.
