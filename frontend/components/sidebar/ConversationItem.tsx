import React from "react";
import { ConversationDTO } from "../../lib/types";
import { Avatar } from "../ui/Avatar";
import { cn } from "../../lib/utils";

interface ConversationItemProps {
  conversation: ConversationDTO;
  active?: boolean;
  onClick: () => void;
}

export function ConversationItem({ conversation, active, onClick }: ConversationItemProps) {
  const title =
    conversation.name ||
    conversation.participants.map((p) => p.displayName || p.username).join(", ");

  return (
    <button
      onClick={onClick}
      className={cn(
        "flex w-full items-center gap-3 px-4 py-3 text-left transition-all duration-200",
        active
          ? "border-l-2 border-[var(--color-fern)] bg-gradient-to-r from-[rgba(229,217,182,0.15)] to-[rgba(164,190,123,0.1)]"
          : "hover:bg-gradient-to-r hover:from-[rgba(229,217,182,0.08)] hover:to-[rgba(164,190,123,0.05)]"
      )}
    >
      <Avatar name={title || "Conversation"} />
      <div className="flex-1">
        <div className="text-sm font-semibold text-[var(--color-parchment)] line-clamp-1">
          {title || "Untitled"}
        </div>
        <div className="text-xs text-[var(--color-text-muted)]">
          {conversation.type === "GROUP" ? "Group" : "Private"}
        </div>
      </div>
    </button>
  );
}

