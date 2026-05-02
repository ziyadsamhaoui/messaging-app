"use client";

import React, { useState } from "react";
import { Button } from "../ui/Button";
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
    <div className="border-t border-[var(--color-border)] bg-[rgba(26,58,32,0.8)] p-4">
      <div className="flex items-end gap-3">
        <Textarea
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder="Write your message…"
          className="min-h-[60px] flex-1"
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault();
              handleSend();
            }
          }}
        />
        <Button onClick={handleSend} disabled={disabled}>
          Send
        </Button>
      </div>
    </div>
  );
}

