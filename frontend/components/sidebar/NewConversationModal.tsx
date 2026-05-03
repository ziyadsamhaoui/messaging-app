"use client";

import React, { useState } from "react";
import { Modal } from "../ui/Modal";
import { Input } from "../ui/Input";
import { CreateConversationRequest } from "../../lib/types";

interface NewConversationModalProps {
  open: boolean;
  onClose: () => void;
  onCreate: (body: CreateConversationRequest) => Promise<void>;
}

export function NewConversationModal({ open, onClose, onCreate }: NewConversationModalProps) {
  const [type, setType] = useState<"PRIVATE" | "GROUP">("PRIVATE");
  const [targetUsername, setTargetUsername] = useState("");
  const [participants, setParticipants] = useState("");
  const [name, setName] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleCreate() {
    setLoading(true);
    try {
      if (type === "PRIVATE") {
        await onCreate({ type, targetUsername: targetUsername.trim() });
      } else {
        const list = participants
          .split(",")
          .map((p) => p.trim())
          .filter(Boolean);
        await onCreate({ type, participantUsernames: list, name: name.trim() || undefined });
      }
      onClose();
      setTargetUsername("");
      setParticipants("");
      setName("");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal open={open} onClose={onClose} title="New Conversation">
      <div className="flex flex-col gap-4">
        <div className="flex items-center gap-2 rounded-2xl bg-gradient-to-r from-[rgba(95,141,78,0.2)] to-[rgba(164,190,123,0.1)] p-1">
          <button
            type="button"
            onClick={() => setType("PRIVATE")}
            className={`flex-1 rounded-xl px-3 py-2 text-sm transition-all ${
              type === "PRIVATE"
                ? "bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] text-[var(--color-parchment)]"
                : "text-[rgba(229,217,182,0.7)]"
            }`}
          >
            Private
          </button>
          <button
            type="button"
            onClick={() => setType("GROUP")}
            className={`flex-1 rounded-xl px-3 py-2 text-sm transition-all ${
              type === "GROUP"
                ? "bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] text-[var(--color-parchment)]"
                : "text-[rgba(229,217,182,0.7)]"
            }`}
          >
            Group
          </button>
        </div>

        {type === "PRIVATE" ? (
          <Input
            label="Target username"
            value={targetUsername}
            onChange={(e) => setTargetUsername(e.target.value)}
            placeholder="e.g. fernwalker"
            className="bg-[rgba(26,58,32,0.6)]"
          />
        ) : (
          <>
            <Input
              label="Group name (optional)"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Tea Circle"
              className="bg-[rgba(26,58,32,0.6)]"
            />
            <Input
              label="Participants (comma-separated)"
              value={participants}
              onChange={(e) => setParticipants(e.target.value)}
              placeholder="sage, moss, river"
              className="bg-[rgba(26,58,32,0.6)]"
            />
            <p className="text-xs text-[rgba(164,190,123,0.7)]">Max 50 participants.</p>
          </>
        )}

        <div className="flex justify-end gap-2">
          <button
            type="button"
            onClick={onClose}
            className="text-sm text-[rgba(229,217,182,0.6)] hover:text-[var(--color-parchment)]"
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={handleCreate}
            disabled={loading}
            className="rounded-xl bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] px-4 py-2 text-sm font-semibold text-[var(--color-parchment)] transition-all hover:scale-[1.02] disabled:opacity-60"
          >
            {loading ? "Creating…" : "Create"}
          </button>
        </div>
      </div>
    </Modal>
  );
}

