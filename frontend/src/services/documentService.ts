import api, { API_BASE_URL } from "./api";

export interface DocItem {
  id: string;
  name: string;
}

export async function uploadDocument(
  file: File,
  onProgress?: (pct: number) => void
): Promise<string> {
  const form = new FormData();
  form.append("file", file);
  const { data } = await api.post<string>("/user/upload", form, {
    onUploadProgress: (e) => {
      if (e.total && onProgress) onProgress(Math.round((e.loaded / e.total) * 100));
    },
  });
  return data;
}

export async function getDocuments(): Promise<DocItem[]> {
  const { data } = await api.get<Array<Record<string, string>>>("/user/getDocDetails");
  return data.map((row) => {
    const [id, name] = Object.entries(row)[0];
    return { id, name };
  });
}

export async function streamAnswer(
  docId: string,
  question: string,
  onChunk: (text: string) => void,
  signal?: AbortSignal
): Promise<void> {
  const token = window.localStorage.getItem("token");
  const res = await fetch(`${API_BASE_URL}/user/ask/${docId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "text/event-stream",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify({ question }),
    signal,
  });

  if (!res.ok || !res.body) {
    throw new Error(`Stream failed (${res.status})`);
  }

  const reader = res.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";

  while (true) {
    const { done, value } = await reader.read();
    if (done) break;
    buffer += decoder.decode(value, { stream: true });

    // Try SSE-style framing first: events end with \n\n
    let sepIndex;
    while ((sepIndex = buffer.indexOf("\n\n")) !== -1) {
      const rawEvent = buffer.slice(0, sepIndex);
      buffer = buffer.slice(sepIndex + 2);
      const lines = rawEvent.split("\n");
      const dataLines = lines
        .filter((l) => l.startsWith("data:"))
        .map((l) => l.replace(/^data:\s?/, ""));
      if (dataLines.length > 0) {
        onChunk(dataLines.join("\n"));
      } else if (rawEvent.length > 0) {
        // Plain text line
        onChunk(rawEvent);
      }
    }
  }

  // Flush remainder if backend sent plain (non-SSE) text
  if (buffer.length > 0) {
    if (buffer.startsWith("data:")) {
      onChunk(buffer.replace(/^data:\s?/, ""));
    } else {
      onChunk(buffer);
    }
  }
}