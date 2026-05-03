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
			<div className="absolute left-10 top-16 h-24 w-24 rounded-full bg-[rgba(164,190,123,0.1)] blur-3xl animate-bounce" style={{ animationDelay: "200ms" }} />
			<div className="absolute right-10 top-28 h-20 w-20 rounded-full bg-[rgba(95,141,78,0.15)] blur-3xl animate-bounce" style={{ animationDelay: "500ms" }} />
			<div className="absolute bottom-20 left-28 h-28 w-28 rounded-full bg-[rgba(229,217,182,0.1)] blur-3xl animate-bounce" style={{ animationDelay: "700ms" }} />
			<div className="absolute bottom-16 right-20 h-24 w-24 rounded-full bg-[rgba(40,84,48,0.2)] blur-3xl animate-bounce" style={{ animationDelay: "1000ms" }} />

			<header className="relative z-20 flex items-center justify-between border-b border-[rgba(229,217,182,0.1)] bg-gradient-to-r from-[rgba(40,84,48,0.8)] to-[rgba(26,58,32,0.6)] px-6 py-4 backdrop-blur-sm">
				<Link href="/" className="font-display text-lg font-bold text-transparent bg-gradient-to-r from-[var(--color-parchment)] to-[var(--color-sage)] bg-clip-text">
					BadrLink
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
			</header>

			<main className="relative z-10 flex min-h-[calc(100vh-72px)] flex-col items-center justify-center px-6">
				<div className="relative flex w-full max-w-5xl flex-col items-center text-center">
					<div className="absolute left-[3%] top-8 hidden rounded-2xl border border-[rgba(164,190,123,0.2)] bg-gradient-to-br from-[rgba(40,84,48,0.8)] to-[rgba(95,141,78,0.6)] px-4 py-3 text-sm text-[var(--color-parchment)] backdrop-blur-sm opacity-0 animate-fade-up md:flex" style={{ animationDelay: "300ms" }}>
						<div className="mr-3 h-8 w-8 rounded-full bg-gradient-to-br from-[var(--color-sage)] to-[var(--color-fern)]" />
						Hey, you free tonight? 👋
					</div>
					<div className="absolute left-[6%] top-40 hidden rounded-2xl border border-[rgba(164,190,123,0.2)] bg-gradient-to-br from-[rgba(40,84,48,0.8)] to-[rgba(95,141,78,0.6)] px-4 py-3 text-sm text-[var(--color-parchment)] backdrop-blur-sm opacity-0 animate-fade-up md:flex" style={{ animationDelay: "500ms" }}>
						Miss talking to you 🌿
					</div>
					<div className="absolute right-[3%] top-12 hidden items-center gap-3 rounded-2xl border border-[rgba(229,217,182,0.3)] bg-gradient-to-tr from-[rgba(164,190,123,0.7)] to-[rgba(229,217,182,0.6)] px-4 py-3 text-sm text-[var(--color-forest)] backdrop-blur-sm opacity-0 animate-fade-up md:flex" style={{ animationDelay: "400ms" }}>
						Always for you 😊
						<div className="h-8 w-8 rounded-full bg-gradient-to-br from-[rgba(229,217,182,0.9)] to-[rgba(164,190,123,0.7)]" />
					</div>
					<div className="absolute right-[6%] top-44 hidden items-center gap-3 rounded-2xl border border-[rgba(229,217,182,0.3)] bg-gradient-to-tr from-[rgba(164,190,123,0.7)] to-[rgba(229,217,182,0.6)] px-4 py-3 text-sm text-[var(--color-forest)] backdrop-blur-sm opacity-0 animate-fade-up md:flex" style={{ animationDelay: "700ms" }}>
						Same time tomorrow? ✨
						<div className="h-8 w-8 rounded-full bg-gradient-to-br from-[rgba(229,217,182,0.9)] to-[rgba(164,190,123,0.7)]" />
					</div>

					<motion.h1
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8 }}
						className="font-display text-5xl font-bold text-transparent bg-gradient-to-r from-[var(--color-parchment)] via-[var(--color-sage)] to-[var(--color-parchment)] bg-clip-text sm:text-6xl lg:text-7xl"
					>
						Where words feel closer.
						<br />
						<span className="italic">And people do too.</span>
					</motion.h1>

					<motion.p
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8, delay: 0.15 }}
						className="mt-4 max-w-2xl text-base text-[rgba(164,190,123,0.8)] lg:text-lg"
					>
						A web-based app that keeps you connected with the people who matter most.
					</motion.p>

					<motion.div
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8, delay: 0.3 }}
						className="mt-6 flex flex-col items-center gap-3 sm:flex-row"
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

					<motion.div
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8, delay: 0.4 }}
						className="mt-8"
					>
						<Image
							src="/app/texting_image.png"
							alt="Texting illustration"
							width={224}
							height={224}
							className="h-40 w-40 object-contain drop-shadow-[0_8px_24px_rgba(95,141,78,0.3)] md:h-56 md:w-56"
						/>
					</motion.div>

					<motion.p
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8, delay: 0.5 }}
						className="mt-6 font-display text-lg italic text-transparent bg-gradient-to-r from-[var(--color-parchment)] via-[var(--color-sage)] to-[var(--color-parchment)] bg-clip-text md:text-xl"
					>
						&ldquo;Closer than a whisper, warmer than a thought.&rdquo;
					</motion.p>

					<motion.div
						initial={{ opacity: 0, y: 16 }}
						animate={{ opacity: 1, y: 0 }}
						transition={{ duration: 0.8, delay: 0.6 }}
						className="mt-8 flex flex-col gap-3 text-sm text-[rgba(229,217,182,0.7)] sm:flex-row"
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
			</main>
		</div>
	);
}
