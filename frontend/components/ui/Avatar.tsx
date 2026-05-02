import React from "react";
import { cn } from "../../lib/utils";

export function Avatar({ name, className }: { name: string; className?: string }) {
  const initials = name
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase())
    .join("");

  return (
    <div
      className={cn(
        "flex h-9 w-9 items-center justify-center rounded-full bg-[rgba(164,190,123,0.25)] text-xs font-semibold text-[var(--color-parchment)]",
        className
      )}
    >
      {initials || "?"}
    </div>
  );
}

