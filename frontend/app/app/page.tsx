"use client";

import React, { useMemo, useState, useEffect } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useAuth } from "../../lib/auth";
import { useConversations } from "../../hooks/useConversations";
import { useMessages } from "../../hooks/useMessages";
import { useStomp } from "../../hooks/useStomp";
import { ConversationDTO, MessageDTO } from "../../lib/types";
import { createConversation, sendMessage, ApiError } from "../../lib/api";
import { AppShell } from "../../components/layout/AppShell";
import { ConversationItem } from "../../components/sidebar/ConversationItem";
import { NewConversationModal } from "../../components/sidebar/NewConversationModal";
import { MessageList } from "../../components/chat/MessageList";
import { MessageInput } from "../../components/chat/MessageInput";
import { Input } from "../../components/ui/Input";
import { Skeleton } from "../../components/ui/Skeleton";
import { useToast } from "../../components/ui/Toast";

export default function MessagingApp() {
  const router = useRouter();
  const auth = useAuth();
  const toast = useToast();
  const [selected, setSelected] = useState<ConversationDTO | null>(null);
  const [query, setQuery] = useState("");
  const [modalOpen, setModalOpen] = useState(false);

  const { items: conversations, loading: loadingConversations, nextCursor, loadMore, setItems } =
    useConversations(auth.token);

  const { items: messages, loading: loadingMessages, nextCursor: messageCursor, loadMore: loadMoreMessages, appendMessage, setItems: setMessageItems } =
    useMessages(auth.token, selected?.conversationId ?? null);

  const { subscribe } = useStomp({
    token: auth.token,
    onError: (msg) => toast.push(msg, "error"),
    onConnect: (client) => {
      client.subscribe("/user/queue/errors", (message) => {
        try {
          const payload = JSON.parse(message.body);
          toast.push(payload.message || "An error occurred", payload.status === 200 ? "success" : "error");
          if (payload.status === 401) {
            auth.logout();
            router.push("/login");
          }
        } catch {
          toast.push("WebSocket error", "error");
        }
      });

      client.subscribe("/topic/conversations", (message) => {
        try {
          const convo = JSON.parse(message.body) as ConversationDTO;
          setItems((prev) => {
            if (prev.some((c) => c.conversationId === convo.conversationId)) return prev;
            return [convo, ...prev];
          });
        } catch {
          // ignore parse errors
        }
      });

      if (selected?.conversationId) {
        client.subscribe(`/topic/conversations/${selected.conversationId}`, (message) => {
          const msg = JSON.parse(message.body) as MessageDTO;
          appendMessage(msg);
        });
      }
    },
  });

  useEffect(() => {
    if (!selected) return;
    setMessageItems([]);
    const sub = subscribe(`/topic/conversations/${selected.conversationId}`, (message) => {
      const msg = JSON.parse(message.body) as MessageDTO;
      appendMessage(msg);
    });
    return () => {
      sub?.unsubscribe();
    };
  }, [selected?.conversationId, subscribe, appendMessage, setMessageItems, selected]);

  useEffect(() => {
    if (!auth.token) {
      router.push("/login");
    }
  }, [auth.token, router]);

  const filtered = useMemo(() => {
    if (!query.trim()) return conversations;
    const q = query.toLowerCase();
    return conversations.filter((c) => {
      const title = c.name || c.participants.map((p) => p.displayName || p.username).join(", ");
      return title.toLowerCase().includes(q);
    });
  }, [conversations, query]);

  if (!auth.token) {
    return null;
  }

  async function handleCreate(body: Parameters<typeof createConversation>[1]) {
    if (!auth.token) return;
    try {
      const conversation = await createConversation(auth.token, body);
      setItems((prev) => [conversation, ...prev]);
      setSelected(conversation);
    } catch (err) {
      if (err instanceof ApiError) {
        toast.push(err.payload?.message || err.message, "error");
      } else {
        toast.push("Failed to create conversation", "error");
      }
    }
  }

  async function handleSend(content: string) {
    if (!auth.token || !selected) return;
    try {
      const msg = await sendMessage(auth.token, selected.conversationId, content);
      appendMessage(msg);
    } catch (err) {
      if (err instanceof ApiError) {
        toast.push(err.payload?.message || err.message, "error");
      } else {
        toast.push("Failed to send message", "error");
      }
    }
  }

  const sidebarContent = (
    <div className="flex h-full flex-col">
      <div className="border-b border-[rgba(229,217,182,0.1)] px-5 py-5">
        <div className="font-display text-xl text-transparent bg-gradient-to-r from-[var(--color-parchment)] to-[var(--color-sage)] bg-clip-text">
          BadrLink
        </div>
        <p className="text-xs text-[rgba(164,190,123,0.7)]">{auth.username}</p>
      </div>
      <div className="px-4 pt-4">
        <Input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search"
          className="rounded-xl border border-[rgba(164,190,123,0.15)] bg-gradient-to-r from-[rgba(46,94,55,0.6)] to-[rgba(95,141,78,0.2)] text-[var(--color-parchment)] placeholder:text-[rgba(164,190,123,0.5)]"
        />
      </div>
      <div className="px-4 pt-3">
        <button
          type="button"
          onClick={() => setModalOpen(true)}
          className="w-full rounded-xl bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] px-4 py-2 text-sm font-medium text-[var(--color-parchment)] transition-all hover:scale-[1.02] hover:shadow-[0_8px_20px_rgba(95,141,78,0.2)] active:scale-[0.98]"
        >
          + New Chat
        </button>
      </div>
      <div className="mt-3 flex-1 space-y-1 overflow-y-auto px-2 pb-4">
        {loadingConversations && conversations.length === 0 ? (
          <div className="space-y-2 px-2">
            {Array.from({ length: 5 }).map((_, i) => (
              <Skeleton key={i} className="h-14" />
            ))}
          </div>
        ) : filtered.length === 0 ? (
          <div className="px-3 text-sm text-[rgba(164,190,123,0.6)]">No conversations yet.</div>
        ) : (
          filtered.map((conversation) => (
            <ConversationItem
              key={conversation.conversationId}
              conversation={conversation}
              active={selected?.conversationId === conversation.conversationId}
              onClick={() => setSelected(conversation)}
            />
          ))
        )}
      </div>
      {nextCursor && (
        <button
          type="button"
          onClick={() => loadMore()}
          className="mx-4 mb-4 rounded-xl border border-[rgba(229,217,182,0.25)] px-4 py-2 text-sm text-[rgba(229,217,182,0.7)] transition-all hover:bg-[rgba(229,217,182,0.08)]"
        >
          Load more
        </button>
      )}
    </div>
  );

  const mainContent = (
    <div className="flex h-full flex-col bg-gradient-to-br from-[rgba(229,217,182,0.95)] to-[rgba(212,200,158,0.9)]">
      <header className="flex items-center justify-between border-b border-[rgba(40,84,48,0.15)] bg-gradient-to-r from-[rgba(212,200,158,0.9)] to-[rgba(229,217,182,0.95)] px-6 py-4">
        <div className="flex items-center gap-3">
          <div className="h-10 w-10 rounded-full bg-gradient-to-br from-[var(--color-fern)] to-[var(--color-forest)]" />
          <div>
            <div className="font-display text-base text-[var(--color-forest)]">
              {selected
                ? selected.name || selected.participants.map((p) => p.displayName || p.username).join(", ")
                : "Select a conversation"}
            </div>
            {selected && (
              <div className="text-xs text-[rgba(95,141,78,0.7)]">
                {selected.type === "GROUP" ? "Group chat" : "Private chat"}
              </div>
            )}
          </div>
        </div>
        <button
          type="button"
          onClick={() => {
            auth.logout();
            router.push("/login");
          }}
          className="rounded-xl border border-[rgba(40,84,48,0.2)] px-4 py-2 text-xs text-[rgba(40,84,48,0.7)] transition-all hover:bg-[rgba(40,84,48,0.08)]"
        >
          Logout
        </button>
      </header>

      {selected ? (
        <>
          <MessageList
            messages={messages}
            currentUserId={auth.userId}
            loading={loadingMessages}
            hasMore={Boolean(messageCursor)}
            onLoadMore={loadMoreMessages}
          />
          <MessageInput onSend={handleSend} />
        </>
      ) : (
        <div className="flex flex-1 flex-col items-center justify-center gap-4 text-sm text-[rgba(40,84,48,0.4)]">
          <Image
            src="/app/texting_image.png"
            alt="Select a conversation"
            width={128}
            height={128}
            className="h-32 w-32 opacity-40"
          />
          <span className="font-display italic">Select a conversation to start chatting.</span>
        </div>
      )}
    </div>
  );

  return (
    <div className="h-screen">
      <div className="lg:hidden h-full">
        {!selected ? (
          <div className="h-full">{sidebarContent}</div>
        ) : (
          <div className="flex h-full flex-col">
            <div className="flex items-center gap-3 border-b border-[rgba(40,84,48,0.15)] bg-gradient-to-r from-[rgba(212,200,158,0.9)] to-[rgba(229,217,182,0.95)] px-4 py-3">
              <button
                type="button"
                onClick={() => setSelected(null)}
                className="rounded-xl border border-[rgba(40,84,48,0.2)] px-3 py-1 text-xs text-[rgba(40,84,48,0.7)]"
              >
                ← Back
              </button>
              <div className="text-sm text-[rgba(40,84,48,0.7)]">Conversation</div>
            </div>
            {mainContent}
          </div>
        )}
      </div>

      <div className="hidden h-full lg:block">
        <AppShell
          sidebar={sidebarContent}
          main={mainContent}
          details={
            <div className="flex h-full flex-col gap-4 p-4">
              <div className="rounded-3xl border border-[rgba(164,190,123,0.2)] bg-gradient-to-br from-[rgba(26,58,32,0.9)] to-[rgba(40,84,48,0.8)] p-4">
                <h3 className="text-sm font-semibold text-[var(--color-parchment)]">Details</h3>
                <p className="mt-2 text-xs text-[rgba(164,190,123,0.7)]">
                  Select a conversation to see participants and shared moments.
                </p>
              </div>
            </div>
          }
        />
      </div>

      <NewConversationModal open={modalOpen} onClose={() => setModalOpen(false)} onCreate={handleCreate} />
    </div>
  );
}
