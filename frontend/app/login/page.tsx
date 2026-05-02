"use client";

import React, { useState } from "react";
import Link from "next/link";
import { motion } from "motion/react";
import { Input } from "../../components/ui/Input";
import { Button } from "../../components/ui/Button";
import { login, register, ApiError } from "../../lib/api";
import { useAuth } from "../../lib/auth";
import { useRouter } from "next/navigation";
import { useToast } from "../../components/ui/Toast";

export default function LoginPage() {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const auth = useAuth();
  const router = useRouter();
  const toast = useToast();

  const strengthScore = React.useMemo(() => {
    let score = 0;
    if (password.length >= 8) score += 1;
    if (/[A-Z]/.test(password)) score += 1;
    if (/[0-9]/.test(password)) score += 1;
    if (/[^A-Za-z0-9]/.test(password)) score += 1;
    return score;
  }, [password]);

  const strengthLabel = ["Weak", "Fair", "Strong", "Strong"][strengthScore] || "Weak";

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    if (mode === "register" && password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      const res = mode === "login"
        ? await login(username, password)
        : await register(username, password);
      auth.login(res.token, res.username, res.userId);
      toast.push("Welcome to the canopy.", "success");
      router.push("/app");
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.payload?.message || err.message);
      } else {
        setError("Something went wrong.");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen bg-[var(--color-parchment)] text-[var(--color-forest)]">
      <div className="px-6 py-6">
        <Link href="/" className="font-display text-lg text-[var(--color-forest)]">
          Verdant Messages
        </Link>
      </div>

      <div className="mx-auto flex min-h-[calc(100vh-96px)] w-full max-w-5xl items-center justify-center px-6 pb-12">
        <motion.div
          initial={{ opacity: 0, y: 18 }}
          animate={{ opacity: 1, y: 0 }}
          className="w-full max-w-md rounded-2xl border border-[rgba(40,84,48,0.2)] bg-[var(--color-forest)] p-8 text-[var(--color-parchment)] shadow-[0_8px_40px_rgba(40,84,48,0.25)]"
        >
          <div className="space-y-2">
            <h1 className="font-display text-3xl">
              {mode === "login" ? "Welcome Back" : "Create Your Account"}
            </h1>
            <p className="text-sm text-[var(--color-sage)]">
              {mode === "login" ? "Good to see you again." : "Join and start chatting."}
            </p>
            <div className="h-px w-full bg-[rgba(164,190,123,0.35)]" />
          </div>

          <form className="mt-6 flex flex-col gap-4" onSubmit={handleSubmit}>
            <div className="relative">
              <Input
                label="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="forestwalker"
                required
                className="bg-[var(--color-input-dark)] pr-10"
              />
              <span className="pointer-events-none absolute right-4 top-10 text-[var(--color-sage)]">👤</span>
            </div>
            <div className="relative">
              <Input
                label="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
                className="bg-[var(--color-input-dark)] pr-10"
              />
              <span className="pointer-events-none absolute right-4 top-10 text-[var(--color-sage)]">👁</span>
            </div>

            {mode === "register" && (
              <>
                <Input
                  label="Confirm password"
                  type="password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="••••••••"
                  required
                  className="bg-[var(--color-input-dark)]"
                />
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-xs text-[var(--color-sage)]">
                    <span>Password strength</span>
                    <span>{strengthLabel}</span>
                  </div>
                  <div className="grid grid-cols-4 gap-2">
                    {Array.from({ length: 4 }).map((_, i) => (
                      <div
                        key={i}
                        className={`h-2 rounded-full ${
                          i < strengthScore
                            ? i >= 2
                              ? "bg-[var(--color-fern-dark)]"
                              : "bg-[var(--color-sage)]"
                            : "bg-[rgba(229,217,182,0.25)]"
                        }`}
                      />
                    ))}
                  </div>
                </div>
              </>
            )}

            {error && <div className="text-sm text-[var(--color-error)]">{error}</div>}
            <Button type="submit" disabled={loading} className="w-full">
              {loading ? "Please wait…" : mode === "login" ? "Log In" : "Sign Up"}
            </Button>
          </form>

          <div className="mt-6 text-xs text-[var(--color-parchment-dim)]">
            {mode === "login" ? "Don't have an account?" : "Already have one?"} {" "}
            <button
              type="button"
              onClick={() => setMode(mode === "login" ? "register" : "login")}
              className="text-[var(--color-sage)]"
            >
              {mode === "login" ? "Create one →" : "Log in →"}
            </button>
          </div>
        </motion.div>
      </div>
    </div>
  );
}
