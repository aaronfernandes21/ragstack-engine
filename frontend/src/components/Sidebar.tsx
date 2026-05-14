import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useTheme } from "../context/ThemeContext";
import { type DocItem } from "../services/documentService";
import UploadBox from "./UploadBox";
import "./Sidebar.css";

interface Props {
  docs: DocItem[];
  loading: boolean;
  activeDocId: string | null;
  onSelect: (doc: DocItem) => void;
  onUploaded: () => void;
}

export default function Sidebar({ docs, loading, activeDocId, onSelect, onUploaded }: Props) {
  const { username, logout } = useAuth();
  const { theme, toggle } = useTheme();
  const [open, setOpen] = useState(false);

  useEffect(() => {
    setOpen(false);
  }, [activeDocId]);

  return (
    <>
      <button className="sidebar-toggle" onClick={() => setOpen((o) => !o)} aria-label="Menu">
        ☰
      </button>
      <aside className={`sidebar ${open ? "open" : ""}`}>
        <div className="sidebar-header">
          <div className="sidebar-brand">
            <span className="sidebar-logo">PD</span>
            <span>PaperDeck</span>
          </div>
          <p className="sidebar-user">Hi, {username ?? "there"}</p>
        </div>

        <UploadBox onUploaded={onUploaded} />

        <div className="sidebar-section-title">Your documents</div>
        <div className="sidebar-docs">
          {loading ? (
            <>
              <div className="skeleton-row" />
              <div className="skeleton-row" />
              <div className="skeleton-row" />
            </>
          ) : docs.length === 0 ? (
            <div className="sidebar-empty">No PDFs yet. Upload your first one above.</div>
          ) : (
            docs.map((d) => (
              <button
                key={d.id}
                className={`doc-item ${activeDocId === d.id ? "active" : ""}`}
                onClick={() => onSelect(d)}
                title={d.name}
              >
                <span className="doc-icon">📄</span>
                <span className="doc-name">{d.name}</span>
              </button>
            ))
          )}
        </div>

        <div className="sidebar-footer">
          <button className="btn btn-ghost btn-block" onClick={toggle}>
            {theme === "light" ? "Dark mode" : " Light mode"}
          </button>
          <button className="btn btn-danger btn-block" onClick={logout}>
            Log out
          </button>
        </div>
      </aside>
      {open && <div className="sidebar-backdrop" onClick={() => setOpen(false)} />}
    </>
  );
}