import { Link } from "@tanstack/react-router";
import { useTheme } from "../context/ThemeContext";
import "./Navbar.css";

export default function Navbar() {
  const { theme, toggle } = useTheme();
  return (
    <header className="nav">
      <Link to="/" className="nav-brand">
        <span className="nav-logo">PD</span>
        <span>PaperDeck AI</span>
      </Link>
      <nav className="nav-links">
        <button className="nav-icon-btn" onClick={toggle} aria-label="Toggle theme">
          {theme === "light" ? "🌙" : "☀️"}
        </button>
        <Link to="/login" className="btn btn-ghost">Log in</Link>
        <Link to="/signup" className="btn btn-primary">Get started</Link>
      </nav>
    </header>
  );
}