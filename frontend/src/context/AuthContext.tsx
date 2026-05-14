import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";

interface AuthState {
  token: string | null;
  username: string | null;
  isAuthenticated: boolean;
  setSession: (token: string, username: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthState | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);
  const [hydrated, setHydrated] = useState(false);

  useEffect(() => {
    if (typeof window === "undefined") return;
    setToken(window.localStorage.getItem("token"));
    setUsername(window.localStorage.getItem("username"));
    setHydrated(true);
  }, []);

  const setSession = useCallback((t: string, u: string) => {
    window.localStorage.setItem("token", t);
    window.localStorage.setItem("username", u);
    setToken(t);
    setUsername(u);
  }, []);

  const logout = useCallback(() => {
    window.localStorage.removeItem("token");
    window.localStorage.removeItem("username");
    setToken(null);
    setUsername(null);
  }, []);

  const value = useMemo<AuthState>(
    () => ({
      token,
      username,
      isAuthenticated: !!token,
      setSession,
      logout,
    }),
    [token, username, setSession, logout]
  );

  if (!hydrated) {
    return null;
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}