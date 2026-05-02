import React from "react";
import { MessageDTO } from "../../lib/types";
import { cn } from "../../lib/utils";

interface MessageBubbleProps {
  message: MessageDTO;
  isOwn: boolean;
}

export function MessageBubble({ message, isOwn }: MessageBubbleProps) {
  return (
    <div className={cn("flex", isOwn ? "justify-end" : "justify-start")}
    >
      <div
        className={cn(
          "max-w-[80%] rounded-3xl px-4 py-2 text-sm shadow-[0_6px_14px_rgba(0,0,0,0.25)]",
          isOwn
            ? "bg-[var(--color-fern)] text-[var(--color-parchment)]"
            : "bg-[var(--color-parchment)] text-[var(--color-forest)]"
        )}
      >
        <p className="whitespace-pre-wrap leading-relaxed">{message.content}</p>
        <div className={cn("mt-1 text-[10px]", isOwn ? "text-[rgba(229,217,182,0.8)]" : "text-[rgba(40,84,48,0.7)]")}>
          {new Date(message.createdAt).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
        </div>
      </div>
    </div>
  );
}

