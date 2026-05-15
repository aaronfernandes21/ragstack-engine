# ragstack-engine

## AI Document Knowledge System

ragstack-engine is a full-stack Retrieval-Augmented Generation platform designed to enable intelligent interaction with uploaded documents through natural language conversations.
![System Architecture](/img.png)

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

# Prerequisites

Before running the project, ensure the following are installed:

- Java 21 JDK
- Docker Desktop
- Git
- Node.js (LTS)
- Maven

---

# Backend Setup

## 1. Clone Repository

```bash
git clone https://github.com/aaronfernandes21/ragstack-engine
cd aiDoc
```

---

## 2. Configure Environment Variables

Update the following variables inside `docker-compose.yml`:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/app_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29092

SPRING_AI_OLLAMA_BASE_URL=http://ollama:11434
EMBEDDING_API_URL=http://ollama:11434/api/embeddings

JWT_SECRET=your-secret-key
```

---

## 3. Build Spring Boot Application

### Windows

```bash
mvnw.cmd clean package -DskipTests
```

### Linux / macOS

```bash
./mvnw clean package -DskipTests
```

---

## 4. Start Complete Infrastructure

```bash
docker compose up --build
```

This starts:

- Spring Boot Backend
- PostgreSQL + pgvector
- Apache Kafka
- Zookeeper
- Ollama

---

## 5. Pull Ollama Models

Open another terminal and run:

```bash
docker exec -it ollama ollama pull phi3:mini
```

```bash
docker exec -it ollama ollama pull nomic-embed-text
```

---

## 6. Enable pgvector Extension

Connect to PostgreSQL container:

```bash
docker exec -it pgvector-db psql -U postgres -d app_db
```

Run:

```sql
CREATE EXTENSION IF NOT EXISTS vector;

CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS jobs;
CREATE SCHEMA IF NOT EXISTS ai;
```

Exit:

```sql
\q
```

---

## 7. Backend Runs At

```text
http://localhost:8080
```

---

# Frontend Setup

## 1. Move to Frontend folder

```bash
cd ..
cd frontend
```

---

## 2. Install Dependencies

```bash
npm install
```

---

## 3. Start Frontend

```bash
npm run dev
```

Frontend runs at:

```text
http://localhost:3000
```

---



## Purpose

This project was developed as an exploration into AI-integrated backend systems and modern RAG-based architectures, combining practical implementations of semantic search, document intelligence, and conversational AI into a cohesive platform.

Some parts of the frontend were built with AI assistance during development to speed up UI setup and prototyping.
