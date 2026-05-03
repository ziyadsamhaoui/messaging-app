"use client";

import React, { useState } from "react";
import { Textarea } from "../ui/Textarea";

interface MessageInputProps {
  onSend: (content: string) => Promise<void> | void;
  disabled?: boolean;
}

export function MessageInput({ onSend, disabled }: MessageInputProps) {
  const [value, setValue] = useState("");

  async function handleSend() {
    const trimmed = value.trim();
    if (!trimmed) return;
    await onSend(trimmed);
    setValue("");
  }

  return (
    <div className="border-t border-[rgba(40,84,48,0.1)] bg-gradient-to-r from-[rgba(212,200,158,0.8)] to-[rgba(229,217,182,0.9)] px-4 py-4">
      <div className="flex items-end gap-3">
        <Textarea
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder="Type a message..."
          className="min-h-[60px] flex-1 rounded-2xl border border-[rgba(40,84,48,0.15)] bg-gradient-to-r from-[rgba(40,84,48,0.08)] to-[rgba(95,141,78,0.05)] text-[var(--color-forest)] placeholder:text-[rgba(40,84,48,0.4)] focus:ring-2 focus:ring-[rgba(95,141,78,0.3)]"
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault();
              handleSend();
            }
          }}
        />
        <button
          type="button"
          onClick={handleSend}
          disabled={disabled}
          className="rounded-xl bg-gradient-to-br from-[var(--color-fern)] to-[var(--color-forest)] p-3 text-[var(--color-parchment)] transition-all hover:scale-[1.05] hover:shadow-[0_10px_20px_rgba(95,141,78,0.3)] active:scale-[0.95] disabled:opacity-60"
          aria-label="Send message"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" className="h-5 w-5">
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 12h12m0 0-4-4m4 4-4 4" />
          </svg>
        </button>
      </div>
    </div>
  );
}

