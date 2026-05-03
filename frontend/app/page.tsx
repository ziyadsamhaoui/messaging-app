"use client";

import Link from "next/link";
import Image from "next/image";
import { motion } from "motion/react";

const stats = [
	{ label: "Fast enough.", sub: "Real time" },
	{ label: "Private.", sub: "Encrypted" },
	{ label: "Simple.", sub: "No clutter" },
];

export default function Home() {
	return (
		<div className="relative min-h-screen overflow-hidden bg-[url('/images/backgroundforlogin.jpg')] bg-cover bg-center text-[var(--color-parchment)]">
			<div className="absolute inset-0 bg-gradient-to-br from-[rgba(40,84,48,0.7)] via-[rgba(95,141,78,0.4)] to-[rgba(229,217,182,0.3)] animate-pulse" />
			<div className="pointer-events-none absolute -left-1/2 top-24 h-10 w-[200%] bg-gradient-to-r from-transparent via-[rgba(229,217,182,0.18)] to-transparent blur-md animate-scroll-plane" />
			<div className="absolute left-10 top-16 h-24 w-24 rounded-full bg-[rgba(164,190,123,0.1)] blur-3xl animate-bounce" style={{ animationDelay: "200ms" }} />
			<div className="absolute right-10 top-28 h-20 w-20 rounded-full bg-[rgba(95,141,78,0.15)] blur-3xl animate-bounce" style={{ animationDelay: "500ms" }} />
			<div className="absolute bottom-20 left-28 h-28 w-28 rounded-full bg-[rgba(229,217,182,0.1)] blur-3xl animate-bounce" style={{ animationDelay: "700ms" }} />
			<div className="absolute bottom-16 right-20 h-24 w-24 rounded-full bg-[rgba(40,84,48,0.2)] blur-3xl animate-bounce" style={{ animationDelay: "1000ms" }} />

			<nav className="relative z-20 w-full h-17 bg-gradient-to-r from-[#E5D9B6] to-[#D4C89E] border-b border-[rgba(40,84,48,0.06)] py-1 px-6 shadow-sm">
				<div className="w-full flex items-center justify-between">
					<Link href="/" className="flex items-center gap-3">
						<Image
							src="/favicon.png"
							width={60}
							height={60}
							alt="BadrLink favicon"
							className="inline-block"
						/>
						<span className="font-display text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-[var(--color-fern)] via-[var(--color-sage)] to-[var(--color-forest)] animate-pulse">
							BadrLink
						</span>
					</Link>
					<div className="flex items-center gap-3">
						<Link href="/login">
							<button className="rounded-xl bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] px-5 py-2 text-sm font-semibold text-[var(--color-parchment)] transition-all hover:scale-[1.03]">
								Sign Up
							</button>
						</Link>
						<Link href="/login">
							<button className="rounded-xl border border-[rgba(229,217,182,0.4)] px-5 py-2 text-sm text-[rgba(229,217,182,0.8)] transition-all hover:bg-[rgba(229,217,182,0.1)]">
								Log In
							</button>
						</Link>
					</div>
				</div>
			</nav>

			<main className="relative z-10 flex min-h-[calc(100vh-72px)] flex-col items-center justify-center px-6">
				<div className="grid w-full max-w-6xl grid-cols-1 items-center gap-12 lg:grid-cols-2">
					<div className="flex flex-col gap-6 text-left">
						<motion.div
							initial={{ opacity: 0, y: 16 }}
							animate={{ opacity: 1, y: 0 }}
							transition={{ duration: 0.8 }}
							className="space-y-3"
						>
							<div className="text-xs uppercase tracking-[0.3em] text-[rgba(229,217,182,0.7)]">
								Closer than a whisper...
							</div>
							<h1 className="font-display text-4xl font-bold text-transparent bg-gradient-to-r from-[var(--color-parchment)] via-[var(--color-sage)] to-[var(--color-parchment)] bg-clip-text sm:text-5xl lg:text-6xl">
								Where words feel closer.
								<br />
								<span className="italic">And people do too.</span>
							</h1>
							<p className="text-base text-[rgba(164,190,123,0.85)] lg:text-lg">
								A web-based app that keeps you connected with the people who matter most.
							</p>
						</motion.div>

						<motion.div
							initial={{ opacity: 0, y: 16 }}
							animate={{ opacity: 1, y: 0 }}
							transition={{ duration: 0.8, delay: 0.15 }}
							className="flex flex-wrap gap-3"
						>
							<Link href="/login">
								<button className="rounded-2xl bg-gradient-to-r from-[var(--color-fern)] to-[var(--color-sage)] px-8 py-3 text-sm font-semibold text-[var(--color-parchment)] transition-all duration-300 hover:scale-[1.03] hover:shadow-[0_12px_30px_rgba(95,141,78,0.3)] active:scale-[0.97]">
									Get Started →
								</button>
							</Link>
							<button className="rounded-2xl border border-[rgba(229,217,182,0.3)] px-6 py-3 text-sm text-[rgba(229,217,182,0.7)] backdrop-blur-sm transition-all hover:bg-[rgba(229,217,182,0.1)]">
								Why BadrLink?
							</button>
						</motion.div>

						<motion.p
							initial={{ opacity: 0, y: 16 }}
							animate={{ opacity: 1, y: 0 }}
							transition={{ duration: 0.8, delay: 0.3 }}
							className="font-display text-lg italic text-transparent bg-gradient-to-r from-[var(--color-parchment)] via-[var(--color-sage)] to-[var(--color-parchment)] bg-clip-text md:text-xl"
						>
							“Closer than a whisper, warmer than a thought.”
						</motion.p>

						<motion.div
							initial={{ opacity: 0, y: 16 }}
							animate={{ opacity: 1, y: 0 }}
							transition={{ duration: 0.8, delay: 0.45 }}
							className="flex flex-col gap-3 text-sm text-[rgba(229,217,182,0.7)] sm:flex-row"
						>
							{stats.map((stat, index) => (
								<div
									key={stat.label}
									className={`px-4 ${index < stats.length - 1 ? "border-r border-[rgba(229,217,182,0.2)]" : ""}`}
								>
									<div>{stat.label}</div>
									<div className="text-xs text-[rgba(164,190,123,0.6)]">{stat.sub}</div>
								</div>
							))}
						</motion.div>
					</div>

					<div className="relative flex items-center justify-center lg:-translate-x-6">
						<div className="pointer-events-none absolute left-1/2 top-3 -translate-x-1/2 rounded-2xl rounded-br-sm border border-[rgba(164,190,123,0.2)] bg-gradient-to-br from-[rgba(40,84,48,0.8)] to-[rgba(95,141,78,0.6)] px-4 py-3 text-xs text-[var(--color-parchment)] shadow-lg shadow-[rgba(255,0,122,0.2)] max-w-[180px] animate-float lg:left-auto lg:-left-10 lg:top-4 lg:translate-x-0">
							A: You still up?
						</div>
						<div className="pointer-events-none absolute left-1/2 top-20 -translate-x-1/2 rounded-2xl rounded-br-sm border border-[rgba(229,217,182,0.3)] bg-gradient-to-tr from-[rgba(164,190,123,0.7)] to-[rgba(229,217,182,0.6)] px-4 py-3 text-xs text-[var(--color-forest)] shadow-lg shadow-[rgba(255,0,122,0.2)] max-w-[180px] animate-float-slow lg:left-auto lg:-right-8 lg:top-24 lg:translate-x-0">
							B: Yeah, can&apos;t sleep
						</div>
						<div className="pointer-events-none absolute left-1/2 top-40 -translate-x-1/2 rounded-2xl rounded-br-sm border border-[rgba(229,217,182,0.3)] bg-gradient-to-tr from-[rgba(164,190,123,0.7)] to-[rgba(229,217,182,0.6)] px-4 py-3 text-xs text-[var(--color-forest)] shadow-lg shadow-[rgba(255,0,122,0.2)] max-w-[180px] animate-float lg:left-auto lg:-right-2 lg:bottom-10 lg:top-auto lg:translate-x-0">
							A: Same... wanna talk for a bit?
						</div>
						<div className="pointer-events-none absolute left-1/2 top-60 -translate-x-1/2 rounded-2xl rounded-br-sm border border-[rgba(164,190,123,0.2)] bg-gradient-to-br from-[rgba(40,84,48,0.8)] to-[rgba(95,141,78,0.6)] px-4 py-3 text-xs text-[var(--color-parchment)] shadow-lg shadow-[rgba(255,0,122,0.2)] max-w-[180px] animate-float-slow lg:left-auto lg:-left-6 lg:bottom-0 lg:top-auto lg:translate-x-0">
							B: Always
						</div>
						<Image
							src="/texting_image.png"
							alt="Texting illustration"
							width={240}
							height={240}
							className="h-48 w-48 object-contain drop-shadow-[0_8px_24px_rgba(95,141,78,0.3)] md:h-56 md:w-56"
						/>
					</div>
				</div>
			</main>
		</div>
	);
}
