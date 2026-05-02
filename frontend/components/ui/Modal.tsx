"use client";

import React, { useEffect } from "react";
import { cn } from "../../lib/utils";

interface ModalProps {
  open: boolean;
  onClose: () => void;
  title?: string;
  children: React.ReactNode;
  className?: string;
}

export function Modal({ open, onClose, title, children, className }: ModalProps) {
  useEffect(() => {
    function onKey(e: KeyboardEvent) {
      if (e.key === "Escape") onClose();
    }
    if (open) {
      window.addEventListener("keydown", onKey);
    }
    return () => window.removeEventListener("keydown", onKey);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-[rgba(0,0,0,0.55)] px-4">
      <div
        className={cn(
          "w-full max-w-lg rounded-3xl border border-[var(--color-border)] bg-[var(--color-surface)] p-6 shadow-[0_20px_60px_rgba(0,0,0,0.4)]",
          className
        )}
      >
        <div className="flex items-center justify-between">
          {title && <h3 className="text-lg font-semibold text-[var(--color-parchment)]">{title}</h3>}
          <button
            onClick={onClose}
            className="rounded-full px-2 py-1 text-[var(--color-text-secondary)] hover:text-[var(--color-parchment)]"
            aria-label="Close modal"
          >
            ✕
          </button>
        </div>
        <div className="mt-4">{children}</div>
      </div>
    </div>
  );
}

