import React from "react";
import { cn } from "../../lib/utils";

interface AppShellProps {
  sidebar: React.ReactNode;
  main: React.ReactNode;
  details?: React.ReactNode;
}

export function AppShell({ sidebar, main, details }: AppShellProps) {
  return (
    <div className="flex h-full w-full">
      <aside className="hidden h-full w-[320px] flex-shrink-0 border-r border-[var(--color-border)] bg-[rgba(26,58,32,0.8)] lg:block">
        {sidebar}
      </aside>
      <main className={cn("flex h-full flex-1 flex-col", details ? "lg:w-[calc(100%-320px-320px)]" : "")}>{main}</main>
      {details && (
        <aside className="hidden h-full w-[320px] flex-shrink-0 border-l border-[var(--color-border)] bg-[rgba(26,58,32,0.8)] lg:block">
          {details}
        </aside>
      )}
    </div>
  );
}

