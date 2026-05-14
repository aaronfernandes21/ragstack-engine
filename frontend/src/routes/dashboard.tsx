import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useCallback, useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import Sidebar from "../components/Sidebar";
import ChatWindow from "../components/ChatWindow";
import { getDocuments, type DocItem } from "../services/documentService";
import { useToast } from "../context/ToastContext";
import "./dashboard.css";

export const Route = createFileRoute("/dashboard")({
  head: () => ({ meta: [{ title: "Dashboard – PaperDeck AI" }] }),
  component: Dashboard,
});

function Dashboard() {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const { toast } = useToast();

  const [docs, setDocs] = useState<DocItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [active, setActive] = useState<DocItem | null>(null);

  useEffect(() => {
    if (!isAuthenticated) navigate({ to: "/login" });
  }, [isAuthenticated, navigate]);

  const refresh = useCallback(async () => {
    try {
      setLoading(true);
      const list = await getDocuments();
      setDocs(list);
    } catch {
      toast("Could not load documents", "error");
    } finally {
      setLoading(false);
    }
  }, [toast]);

  useEffect(() => {
    if (isAuthenticated) refresh();
  }, [isAuthenticated, refresh]);

  if (!isAuthenticated) return null;

  return (
    <div className="dashboard">
      <Sidebar
        docs={docs}
        loading={loading}
        activeDocId={active?.id ?? null}
        onSelect={setActive}
        onUploaded={refresh}
      />
      <main className="dashboard-main">
        {active ? (
          <ChatWindow doc={active} />
        ) : (
          <div className="dashboard-welcome fade-in">
            <div className="welcome-illu">📚</div>
            <h1>Welcome to PaperDeck AI</h1>
            <p>
              Select a document from the sidebar to start a conversation, or upload a new PDF to
              get started.
            </p>
          </div>
        )}
      </main>
    </div>
  );
}