import api from "./api";

export interface AuthCredentials {
  username: string;
  password: string;
}

export async function signup(credentials: AuthCredentials): Promise<string> {
  const { data } = await api.post<string>("/auth/signup", credentials);
  return data;
}

export async function login(credentials: AuthCredentials): Promise<string> {
  const { data } = await api.post<{ accessToken: string }>("/auth/login", credentials);
  return data.accessToken;
}