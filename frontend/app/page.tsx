"use client";

import Link from "next/link";
import { motion } from "motion/react";
import { Button } from "../components/ui/Button";

const statPills = ["Fast enough.", "Private.", "Simple."];

export default function Home() {
	return (
		<div className="min-h-screen bg-[var(--color-parchment)] text-[var(--color-forest)]">
			<header className="flex items-center justify-between border-b border-[rgba(229,217,182,0.1)] bg-[var(--color-forest)] px-6 py-4">
				<Link
					href="/"
					className="font-display text-lg text-[var(--color-parchment)]"
				>
					Verdant Messages
				</Link>
				<div className="flex items-center gap-3">
					<Link href="/login">
						<Button
							variant="outline"
							className="border-[rgba(229,217,182,0.45)] text-[var(--color-parchment)] hover:border-[var(--color-sage)]"
						>
							Log in
						</Button>
					</Link>
					<Link href="/login">
						<Button className="bg-[var(--color-fern)] text-[var(--color-parchment)] hover:bg-[var(--color-fern-dark)]">
							Sign up
						</Button>
					</Link>
				</div>
			</header>

			<main className="grid min-h-[calc(100vh-72px)] grid-cols-1 lg:grid-cols-2">
				<section className="flex flex-col justify-between bg-[var(--color-forest)] px-6 py-12 text-[var(--color-parchment)] lg:px-16">
					<motion.div
						initial={{ opacity: 0, x: -24 }}
						animate={{ opacity: 1, x: 0 }}
						transition={{ duration: 0.8 }}
						className="space-y-6"
					>
						<h1 className="font-display text-4xl leading-tight md:text-6xl">
							Conversations,
							<br />
							rooted in calm.
						</h1>
						<p className="max-w-md text-base text-[var(--color-sage)] md:text-lg">
							Real-time messaging that feels grounded and private. No noise, no
							clutter — just the canopy.
						</p>
						<div className="flex flex-col gap-3 sm:flex-row">
							<Link href="/login">
								<Button className="bg-[var(--color-fern)] text-[var(--color-parchment)] hover:bg-[var(--color-fern-dark)]">
									Get started →
								</Button>
							</Link>
							<Button
								variant="outline"
								className="border-[rgba(164,190,123,0.6)] text-[var(--color-sage)] hover:border-[var(--color-parchment)]"
							>
								Why Verdant?
							</Button>
						</div>
					</motion.div>

					<div className="mt-10 flex flex-wrap gap-3 text-xs uppercase tracking-[0.2em] text-[var(--color-sage-muted)]">
						{statPills.map((stat) => (
							<span
								key={stat}
								className="rounded-full border border-[rgba(164,190,123,0.35)] px-4 py-2"
							>
								{stat}
							</span>
						))}
					</div>
				</section>

				<section className="flex items-center justify-center bg-[var(--color-parchment)] px-6 py-12">
					<motion.div
						initial={{ opacity: 0, x: 24 }}
						animate={{ opacity: 1, x: 0 }}
						transition={{ duration: 0.8, delay: 0.15 }}
						className="w-full max-w-md rounded-[28px] border border-[rgba(40,84,48,0.2)] bg-[rgba(229,217,182,0.8)] p-6 shadow-[0_24px_70px_rgba(40,84,48,0.25)]"
					>
						<div className="flex items-center gap-3">
							<div className="h-10 w-10 rounded-full bg-[var(--color-sage)]" />
							<div>
								<div className="text-sm font-semibold text-[var(--color-forest)]">
									Fern
								</div>
								<div className="text-xs text-[rgba(40,84,48,0.7)]">
									online
								</div>
							</div>
						</div>

						<div className="mt-6 space-y-3">
							<div className="max-w-[85%] rounded-2xl bg-[var(--color-forest)] px-4 py-3 text-sm text-[var(--color-parchment)] shadow-[0_10px_20px_rgba(40,84,48,0.25)]">
								Morning! The grove is awake.
							</div>
							<div className="ml-auto max-w-[85%] rounded-2xl bg-[var(--color-fern)] px-4 py-3 text-sm text-[var(--color-parchment)] shadow-[0_10px_20px_rgba(40,84,48,0.25)]">
								I can hear the leaves. Ready to talk?
							</div>
							<div className="max-w-[70%] rounded-2xl bg-[var(--color-forest)] px-4 py-3 text-sm text-[var(--color-parchment)]">
								Always.
							</div>
						</div>
					</motion.div>
				</section>
			</main>
		</div>
	);
}
