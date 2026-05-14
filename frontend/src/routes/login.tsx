import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useEffect, useState, type FormEvent } from "react";
import { login } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";
import "./auth.css";

export const Route = createFileRoute("/login")({
  head: () => ({
    meta: [{ title: "Log in – PaperDeck AI" }],
  }),
  component: LoginPage,
});

function LoginPage() {
  const navigate = useNavigate();
  const { isAuthenticated, setSession } = useAuth();
  const { toast } = useToast();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [err, setErr] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (isAuthenticated) navigate({ to: "/dashboard" });
  }, [isAuthenticated, navigate]);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setErr(null);
    if (!username.trim() || !password) {
      setErr("Please enter your username and password.");
      return;
    }
    try {
      setBusy(true);
      const token = await login({ username: username.trim(), password });
      setSession(token, username.trim());
      toast("Welcome back!", "success");
      navigate({ to: "/dashboard" });
    } catch {
      setErr("Invalid credentials. Please try again.");
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="card auth-card fade-in" onSubmit={submit}>
        <Link to="/" className="auth-back">← Back</Link>
        <h1 className="auth-title">Welcome back</h1>
        <p className="auth-sub">Log in to continue chatting with your documents.</p>

        <div className="form-group">
          <label className="form-label" htmlFor="u">Username</label>
          <input
            id="u"
            className="form-input"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
        </div>
        <div className="form-group">
          <label className="form-label" htmlFor="p">Password</label>
          <input
            id="p"
            type="password"
            className="form-input"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
          />
        </div>

        {err && <div className="form-error">{err}</div>}

        <button className="btn btn-primary btn-block" disabled={busy} type="submit">
          {busy ? <span className="spinner" /> : "Log in"}
        </button>

        <p className="auth-foot">
          New here? <Link to="/signup">Create an account</Link>
        </p>
      </form>
    </div>
  );
}