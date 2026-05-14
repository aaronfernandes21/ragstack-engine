import { createFileRoute, Link } from "@tanstack/react-router";
import Navbar from "../components/Navbar";
import "./landing.css";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "PaperDeck AI – Chat with your PDFs" },
      {
        name: "description",
        content:
          "Upload any PDF and have a real-time AI conversation with your documents. Fast, secure, and beautifully simple.",
      },
    ],
  }),
  component: Landing,
});

function Landing() {
  return (
    <div className="landing">
      <Navbar />
      <main className="landing-hero fade-in">
        <span className="landing-eyebrow">AI Document Assistant</span>
        <h1 className="landing-title">
          Chat with your <span className="accent">PDFs</span> like never before.
        </h1>
        <p className="landing-sub">
          Upload research papers, textbooks, or contracts — ask questions in plain English and get
          streamed answers grounded in your documents.
        </p>
        <div className="landing-cta">
          <Link to="/signup" className="btn btn-primary">Get started free</Link>
          <Link to="/login" className="btn btn-ghost">I already have an account</Link>
        </div>

        <section className="landing-features">
          <div className="feature-card">
            <div className="feature-icon">📄</div>
            <h3>Upload anything</h3>
            <p>Drop in a PDF and start asking questions in seconds.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">⚡</div>
            <h3>Real-time streaming</h3>
            <p>Watch answers appear token-by-token, ChatGPT style.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🔒</div>
            <h3>Private & secure</h3>
            <p>JWT-protected sessions keep your documents yours.</p>
          </div>
        </section>
      </main>
    </div>
  );
}
