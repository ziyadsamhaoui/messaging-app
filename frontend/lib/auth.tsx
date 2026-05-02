"use client";

import React, { createContext, useContext, useMemo, useState } from "react";

interface AuthState {
  token: string | null;
  username: string | null;
  userId: number | null;
}

interface AuthContextValue extends AuthState {
  login: (token: string, username: string, userId: number) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [auth, setAuth] = useState<AuthState>({
    token: null,
    username: null,
    userId: null,
  });

  const value = useMemo<AuthContextValue>(
    () => ({
      ...auth,
      login: (token, username, userId) => setAuth({ token, username, userId }),
      logout: () => setAuth({ token: null, username: null, userId: null }),
    }),
    [auth]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}

export function getAuthHeader(token: string | null) {
  if (!token) return {};
  return { Authorization: `Bearer ${token}` };
}

