import React from "react";
import { cn } from "../../lib/utils";

export function Skeleton({ className }: { className?: string }) {
  return (
    <div
      className={cn(
        "animate-pulse rounded-2xl bg-[rgba(229,217,182,0.12)]",
        className
      )}
    />
  );
}

