"use client";

import React, { useMemo, useState, useEffect } from "react";
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
import { Button } from "../../components/ui/Button";
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
    <div className="flex h-full flex-col p-4">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-display text-xl text-[var(--color-parchment)]">Conversations</h2>
          <p className="text-xs text-[var(--color-text-muted)]">{auth.username}</p>
        </div>
        <Button variant="soft" onClick={() => setModalOpen(true)}>
          + New
        </Button>
      </div>
      <div className="mt-4">
        <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search" />
      </div>
      <div className="mt-4 flex-1 space-y-2 overflow-y-auto">
        {loadingConversations && conversations.length === 0 ? (
          <div className="space-y-2">
            {Array.from({ length: 5 }).map((_, i) => (
              <Skeleton key={i} className="h-16" />
            ))}
          </div>
        ) : filtered.length === 0 ? (
          <div className="text-sm text-[var(--color-text-muted)]">No conversations yet.</div>
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
        <Button variant="ghost" onClick={() => loadMore()}>
          Load more
        </Button>
      )}
    </div>
  );

  const mainContent = (
    <div className="flex h-full flex-col">
      <header className="flex items-center justify-between border-b border-[var(--color-border)] px-5 py-4">
        <div>
          <div className="font-display text-lg text-[var(--color-parchment)]">
            {selected
              ? selected.name || selected.participants.map((p) => p.displayName || p.username).join(", ")
              : "Select a conversation"}
          </div>
          {selected && (
            <div className="text-xs text-[var(--color-text-muted)]">
              {selected.type === "GROUP" ? "Group chat" : "Private chat"}
            </div>
          )}
        </div>
        <Button variant="ghost" onClick={() => { auth.logout(); router.push("/login"); }}>
          Logout
        </Button>
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
        <div className="flex flex-1 items-center justify-center text-sm text-[var(--color-text-muted)]">
          Choose a conversation to begin.
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
            <div className="flex items-center gap-3 border-b border-[var(--color-border)] px-4 py-3">
              <Button variant="ghost" onClick={() => setSelected(null)}>
                ← Back
              </Button>
              <div className="text-sm text-[var(--color-text-secondary)]">Conversation</div>
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
              <div className="rounded-3xl border border-[var(--color-border)] bg-[rgba(40,84,48,0.5)] p-4">
                <h3 className="text-sm font-semibold text-[var(--color-parchment)]">Details</h3>
                <p className="mt-2 text-xs text-[var(--color-text-muted)]">
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
