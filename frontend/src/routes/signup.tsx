import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useEffect, useState, type FormEvent } from "react";
import { signup, login } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";
import "./auth.css";

export const Route = createFileRoute("/signup")({
  head: () => ({
    meta: [{ title: "Create account – PaperDeck AI" }],
  }),
  component: SignupPage,
});

function SignupPage() {
  const navigate = useNavigate();
  const { isAuthenticated, setSession } = useAuth();
  const { toast } = useToast();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [err, setErr] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (isAuthenticated) navigate({ to: "/dashboard" });
  }, [isAuthenticated, navigate]);

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    setErr(null);
    if (username.trim().length < 3) return setErr("Username must be at least 3 characters.");
    if (password.length < 6) return setErr("Password must be at least 6 characters.");
    if (password !== confirm) return setErr("Passwords do not match.");

    try {
      setBusy(true);
      await signup({ username: username.trim(), password });
      const token = await login({ username: username.trim(), password });
      setSession(token, username.trim());
      toast("Account created!", "success");
      navigate({ to: "/dashboard" });
    } catch {
      setErr("Could not create the account. Try a different username.");
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="card auth-card fade-in" onSubmit={submit}>
        <Link to="/" className="auth-back">← Back</Link>
        <h1 className="auth-title">Create your account</h1>
        <p className="auth-sub">Start chatting with your PDFs in under a minute.</p>

        <div className="form-group">
          <label className="form-label" htmlFor="u">Username</label>
          <input id="u" className="form-input" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div className="form-group">
          <label className="form-label" htmlFor="p">Password</label>
          <input id="p" type="password" className="form-input" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <div className="form-group">
          <label className="form-label" htmlFor="c">Confirm password</label>
          <input id="c" type="password" className="form-input" value={confirm} onChange={(e) => setConfirm(e.target.value)} />
        </div>

        {err && <div className="form-error">{err}</div>}

        <button className="btn btn-primary btn-block" disabled={busy} type="submit">
          {busy ? <span className="spinner" /> : "Create account"}
        </button>

        <p className="auth-foot">
          Already have one? <Link to="/login">Log in</Link>
        </p>
      </form>
    </div>
  );
}