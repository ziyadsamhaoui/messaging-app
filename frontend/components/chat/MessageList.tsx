"use client";

import React, { useEffect, useMemo, useRef } from "react";
import { MessageDTO } from "../../lib/types";
import { MessageBubble } from "./MessageBubble";
import { Skeleton } from "../ui/Skeleton";

interface MessageListProps {
  messages: MessageDTO[];
  currentUserId: number | null;
  loading: boolean;
  hasMore: boolean;
  onLoadMore: () => void;
}

export function MessageList({ messages, currentUserId, loading, hasMore, onLoadMore }: MessageListProps) {
  const containerRef = useRef<HTMLDivElement | null>(null);

  const ordered = useMemo(() => {
    // API returns newest first; display oldest first.
    return [...messages].reverse();
  }, [messages]);

  useEffect(() => {
    const el = containerRef.current;
    if (!el) return;
    const onScroll = () => {
      if (el.scrollTop === 0 && hasMore && !loading) {
        onLoadMore();
      }
    };
    el.addEventListener("scroll", onScroll);
    return () => el.removeEventListener("scroll", onScroll);
  }, [hasMore, loading, onLoadMore]);

  return (
    <div ref={containerRef} className="flex-1 overflow-y-auto px-4 pb-4 pt-2">
      {loading && messages.length === 0 ? (
        <div className="space-y-3">
          {Array.from({ length: 6 }).map((_, i) => (
            <Skeleton key={i} className="h-12 w-[70%]" />
          ))}
        </div>
      ) : ordered.length === 0 ? (
        <div className="flex h-full flex-col items-center justify-center text-sm text-[var(--color-text-muted)]">
          Say something... the forest is listening.
        </div>
      ) : (
        <div className="space-y-3">
          {ordered.map((msg) => (
            <MessageBubble key={msg.messageId} message={msg} isOwn={msg.userId === currentUserId} />
          ))}
          {loading && <div className="text-center text-xs text-[var(--color-text-muted)]">Loading more…</div>}
        </div>
      )}
    </div>
  );
}

