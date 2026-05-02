"use client";

import React, { useState } from "react";
import { Modal } from "../ui/Modal";
import { Button } from "../ui/Button";
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
    <Modal open={open} onClose={onClose} title="Start a new conversation">
      <div className="flex flex-col gap-4">
        <div className="flex items-center gap-2">
          <Button variant={type === "PRIVATE" ? "primary" : "soft"} onClick={() => setType("PRIVATE")}>
            Private
          </Button>
          <Button variant={type === "GROUP" ? "primary" : "soft"} onClick={() => setType("GROUP")}>
            Group
          </Button>
        </div>

        {type === "PRIVATE" ? (
          <Input
            label="Target username"
            value={targetUsername}
            onChange={(e) => setTargetUsername(e.target.value)}
            placeholder="e.g. fernwalker"
          />
        ) : (
          <>
            <Input
              label="Group name (optional)"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Tea Circle"
            />
            <Input
              label="Participants (comma-separated)"
              value={participants}
              onChange={(e) => setParticipants(e.target.value)}
              placeholder="sage, moss, river"
            />
            <p className="text-xs text-[var(--color-text-muted)]">Max 50 participants.</p>
          </>
        )}

        <div className="flex justify-end gap-2">
          <Button variant="ghost" onClick={onClose}>Cancel</Button>
          <Button onClick={handleCreate} disabled={loading}>
            {loading ? "Creating…" : "Create"}
          </Button>
        </div>
      </div>
    </Modal>
  );
}

