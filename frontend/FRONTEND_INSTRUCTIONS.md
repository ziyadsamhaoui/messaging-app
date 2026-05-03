# BadrLink — Frontend Instructions

---

## Design Philosophy & Aesthetic Direction

> **Do not build generic UI. Build something that surprises people.**

You tend to converge toward predictable, "on-distribution" outputs. In frontend design, this manifests as what users call the **"AI slop" aesthetic** — safe choices that look like every other app. Reject this entirely.

### What to Focus On

**Typography**
Choose fonts that are beautiful, unique, and interesting. Avoid overused families like Arial, Inter, Roboto, or system fonts. Opt for distinctive, characterful choices that elevate the aesthetic — pair a bold display/serif font with a refined monospaced or humanist body font. Think editorial, literary, or typographically opinionated.

**Color & Theme**
Commit to a cohesive aesthetic. Use CSS variables for consistency. Dominant colors with sharp accents outperform timid, evenly-distributed palettes. Draw from IDE themes, cultural aesthetics, botanical references, or archival visuals for inspiration. See the palette section below.

**Motion**
Use animations for effects and micro-interactions. Use the **Motion library** for React. Focus on high-impact moments: a single well-orchestrated page-load with staggered reveals (`animation-delay`) creates more delight than scattered micro-interactions. Hover states should feel tactile. Transitions should feel considered.

**Backgrounds & Depth**
Create atmosphere rather than defaulting to solid fills. Layer CSS gradients, geometric patterns, noise textures, or botanical-inspired motifs that match the overall aesthetic. Avoid flat, sterile backgrounds.

**Avoid at all costs:**
- Purple gradients on white/dark backgrounds (cliché)
- Space Grotesk (overused)
- Cookie-cutter card layouts with border-radius: 8px everywhere
- Generic iconography without personality
- Evenly distributed palettes with no dominant anchor
- Nav bar of a darker color with BadrLink in the same color but lighter (e.g., forest navbar with lighter forest text) — this creates a muddy, low-contrast look. Instead, use the **co-dominant color strategy** outlined in the palette section to ensure clear contrast and visual hierarchy, gradient with hover color animation.

**Interpret creatively. Make choices that feel genuinely designed for this specific product.** Vary between light and dark themes, different type hierarchies, different spatial compositions.

---

## App Overview & Pages

This is **BadrLink** — a real-time messaging web app targeting both mobile and desktop. It must be fully responsive from the start (mobile-first). **TailwindCSS is mandatory** for layout and utility classes.

### Pages to Build

| Page | Route | Auth Required |
|------|-------|--------------|
| Landing Page | `/` | No |
| Login / Register | `/login` or `/auth` | No |
| Messaging App | `/app` | Yes |

---

### Page 1 — Landing Page (`/`)

Full-viewport, single-screen. No scrolling content below the fold on desktop. Everything — background, overlays, bubbles, quote, illustration — lives on this one screen.

---

#### Background

- **Base layer:** `bg-[url('/images/backgroundforlogin.jpg')] bg-center bg-cover min-h-screen` — the same background image used on the auth page, providing visual continuity across public pages
- **Animated gradient overlay:** absolute inset div — `bg-gradient-to-br from-forest/70 via-fern/40 to-parchment/30` with a slow `animate-pulse` (6s cycle, subtle)
- **Floating orbs:** 4 absolute-positioned blurred circle divs at staggered positions and opacities — `bg-sage/10`, `bg-fern/15`, `bg-parchment/10`, `bg-forest/20` — each with different `animate-bounce` delays (200ms, 500ms, 700ms, 1000ms) and sizes (`w-24 h-24`, `w-16 h-16`, etc.)
- **Never a plain solid color** on any surface

---

#### Navbar

Sits at the top of the page, `relative z-20`:
- `bg-gradient-to-r from-forest/80 to-forest-deep/60 backdrop-blur-sm`
- `border-b border-parchment/10 px-6 py-4`
- Left: `"BadrLink"` logo — gradient text `from-parchment to-sage bg-clip-text text-transparent`, display font, bold
- Right: `"Sign Up"` button (`bg-gradient-to-r from-fern to-sage text-parchment rounded-xl px-5 py-2 hover:scale-[1.03]`) + `"Log In"` button (outlined, `border border-parchment/40 text-parchment/80 rounded-xl px-5 py-2 hover:bg-parchment/10`)

---

#### Main Content Area

Below the navbar, centered vertically, `relative z-10`:

**Desktop layout:**
```
┌─────────────────────────────────────────────────────────────┐
│  [left bubble 1]                        [right bubble 1]    │
│                                                             │
│  [left bubble 2]   HEADLINE + CTA    [right bubble 2]      │
│                    [texting_image]                          │
│                    quote below                              │
└─────────────────────────────────────────────────────────────┘
```

The headline, CTA, illustration, and quote are **center-stacked**, with the 4 chat bubbles floating absolutely to the left and right — mirroring the reference screenshot's composition.

**Mobile layout:** bubbles hidden, headline + illustration + CTA stacked full-width, quote below.

---

#### Center Stack (Desktop & Mobile)

**Headline:**
- 2–3 lines, large display font — `text-5xl sm:text-6xl lg:text-7xl font-bold`
- Gradient text: `bg-gradient-to-r from-parchment via-sage to-parchment bg-clip-text text-transparent`
- Line 1: plain/bold, Lines 2–3: italic or weight contrast for personality
- Example: *"Where words feel closer. And people do too."*
- Fade-up animation, `animation-delay: 0ms`

**Tagline:**
- 1–2 lines below headline, `text-base lg:text-lg text-sage/80`
- Example: "A web-based app that keeps you connected with the people who matter most."
- Fade-up, `animation-delay: 150ms`

**CTA Buttons (row):**
- Primary: `"Get Started →"` — `bg-gradient-to-r from-fern to-sage text-parchment font-semibold rounded-2xl px-8 py-3 hover:scale-[1.03] hover:shadow-lg hover:shadow-fern/30 active:scale-[0.97] transition-all duration-300`
- Secondary: `"Why BadrLink?"` — `border border-parchment/30 text-parchment/70 rounded-2xl px-6 py-3 backdrop-blur-sm hover:bg-parchment/10 transition-all`
- Fade-up, `animation-delay: 300ms`

**`/app/texting_image.png` illustration:**
- Centered below or overlapping the headline area
- `w-40 h-40 md:w-56 md:h-56 object-contain`
- `drop-shadow-[0_8px_24px_rgba(95,141,78,0.3)]`
- Fade-up, `animation-delay: 400ms`

**Quote:**
- Below illustration: *"Closer than a whisper, warmer than a thought."*
- Italic, display font, `text-lg md:text-xl`
- Gradient text: `bg-gradient-to-r from-parchment via-sage to-parchment bg-clip-text text-transparent`
- Fade-up animation, `animation-delay: 500ms`

**Three micro-stats row:**
- `"Fast enough."` / `"Private."` / `"Simple."` with subdescription in muted sage
- Separated by thin `border-r border-parchment/20` dividers
- `text-parchment/70 text-sm` for labels, `text-sage/60 text-xs` for sub-labels
- Fade-up, `animation-delay: 600ms`

---

#### Decorative Chat Bubbles (Desktop only — `hidden md:flex`)

4 absolutely positioned chat bubbles flanking the center content:

**Left pair** (`absolute left-[3%] md:left-[6%]`):
- Bubble 1 (higher): `bg-gradient-to-br from-forest/80 to-fern/60 backdrop-blur-sm border border-sage/20 rounded-2xl px-4 py-3 text-parchment text-sm`
    - Content: `"Hey, you free tonight? 👋"`
    - Avatar circle left: `bg-gradient-to-br from-sage to-fern w-8 h-8 rounded-full`
- Bubble 2 (lower): same gradient family, slightly different position/opacity
    - Content: `"Miss talking to you 🌿"`
- Fade-in with `animation-delay: 300ms` and `500ms`

**Right pair** (`absolute right-[3%] md:right-[6%]`):
- Bubble 1: `bg-gradient-to-br from-sage/70 to-parchment/60 backdrop-blur-sm border border-parchment/30 rounded-2xl px-4 py-3 text-forest text-sm`
    - Content: `"Always for you 😊"`
    - Avatar circle right: `bg-gradient-to-br from-parchment to-sage w-8 h-8 rounded-full`
- Bubble 2:
    - Content: `"Same time tomorrow? ✨"`
- Fade-in with `animation-delay: 400ms` and `700ms`

Left and right bubble pairs use **different gradient directions** (`to-br` vs `to-tr`) and **different text colors** (parchment vs forest) so they're visually distinct.

---

#### Animations (global CSS)

```css
@keyframes fade-up {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes fade-in {
  from { opacity: 0; transform: translateY(-8px); }
  to   { opacity: 1; transform: translateY(0); }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25%       { transform: translateX(-5px); }
  75%       { transform: translateX(5px); }
}

.animate-fade-up  { animation: fade-up 0.8s ease-out forwards; }
.animate-fade-in  { animation: fade-in 0.6s ease-out forwards; }
.animate-shake    { animation: shake 0.5s ease-in-out; }
```

All elements start `opacity-0` and use staggered `animation-delay` to sequence the reveal.

---

### Page 2 — Login / Register (`/login`)

Same animated background as the landing page (`/images/backgroundforlogin.jpg` + gradient overlay + floating orbs). The content is a single centered auth card with no decorative bubbles or illustration — focused, minimal, immersive.

---

#### Background

Identical to the landing page:
- `bg-[url('/images/backgroundforlogin.jpg')] bg-center bg-cover min-h-screen`
- Animated gradient overlay: `bg-gradient-to-br from-forest/60 via-fern/30 to-parchment/20 animate-pulse`
- 4 floating orbs with staggered `animate-bounce` delays
- All absolute-positioned behind the card (`z-0`)

---

#### Page Header

Top-left logo outside the card, `relative z-10 p-6`:
- `"BadrLink"` — gradient text `from-parchment to-sage`, display font, links to `/`

---

#### Centered Card

`relative z-10`, centered with `flex items-center justify-center min-h-screen`:

**Card container:**
- `bg-gradient-to-br from-forest/80 to-[#1A3A20]/90 backdrop-blur-xl`
- `border border-sage/20 rounded-3xl shadow-2xl`
- `hover:scale-[1.01] hover:shadow-[0_8px_60px_rgba(95,141,78,0.2)] transition-all duration-500`
- `max-w-lg w-full p-8 sm:p-10`

**Icon avatar (top center):**
- `w-20 h-20 rounded-full bg-gradient-to-br from-fern to-forest animate-pulse mx-auto mb-6`
- SVG user icon (login) or user-plus icon (register) in parchment color

**Heading:**
- Login: `"Welcome Back"` / Register: `"Create Your Account"`
- `text-3xl sm:text-4xl font-bold text-center uppercase tracking-widest`
- Gradient text: `bg-gradient-to-r from-parchment via-sage to-parchment bg-clip-text text-transparent`
- `animate-fade-in`

**Subline:**
- Login: `"Good to see you again."` / Register: `"Join and start chatting."`
- `text-sage/80 text-sm tracking-wide text-center mb-6`

**Error banner** (conditional on `error` state):
- `bg-gradient-to-r from-red-900/30 to-red-700/20 border border-red-400/30 text-red-200 rounded-xl p-3 animate-shake`
- SVG warning icon inline

**Success banner** (conditional on `success` state):
- `bg-gradient-to-r from-fern/30 to-sage/20 border border-sage/30 text-parchment rounded-xl p-3 animate-pulse`
- SVG check icon inline

**Input fields — shared styling:**
- Container: `relative group`
- Hover glow layer: absolute inset `bg-gradient-to-r from-fern/20 to-sage/20 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300`
- Input: `w-full bg-forest/40 border border-sage/20 text-parchment placeholder-sage/50 rounded-2xl focus:ring-2 focus:ring-fern/50 focus:border-fern/50 transition-all duration-300 hover:bg-forest/50`
- Leading icon: `absolute left-4 top-1/2 translate-y-[3px] text-sage/60` SVG
- Eye-toggle (password): `absolute right-4 top-1/2 translate-y-[3px] text-sage/60 hover:text-parchment/80`

**Login fields:**
- `h-14` — Username or Email (`👤` icon)
- `h-14` — Password (`🔒` icon) + eye toggle

**Register fields:**
- `grid grid-cols-2 gap-4` row: Display Name (`👤`, `h-12`) + Username (`@`, `h-12`)
- `h-14` — Email address (`✉` icon)
- `h-14` — Password (`🔒`) + eye toggle
- `h-14` — Confirm Password (`🔐`) + eye toggle
- Password strength bar: 4 segments `h-1 rounded-full bg-forest/40`, fill with `bg-gradient-to-r from-fern to-sage` progressively. Label text `text-xs text-sage` — "Weak" / "Fair" / "Strong" / "Excellent"

**CTA Button:**
- `group relative w-full h-14 bg-gradient-to-r from-fern via-sage to-fern text-parchment font-semibold rounded-2xl overflow-hidden`
- `hover:from-fern-dark hover:via-fern hover:to-sage-muted transition-all duration-300`
- `shadow-lg hover:shadow-fern/25 hover:shadow-xl transform hover:scale-[1.02] active:scale-[0.98]`
- Shimmer child: `absolute inset-0 bg-gradient-to-r from-white/0 via-white/10 to-white/0 translate-x-[-100%] group-hover:translate-x-[100%] transition-transform duration-1000`
- Loading: spinner SVG + `"Signing In..."` / `"Creating Account..."`
- Arrow icon `group-hover:translate-x-1 transition-transform`

**Footer toggle:**
- Login: `"Don't have an account?"` + `"Sign Up →"` sage link
- Register: `"Already have an account?"` + `"Sign In →"` sage link
- `text-sage font-semibold hover:text-parchment underline underline-offset-2 hover:underline-offset-4 transition-all`

**All form state via React `useState`. No `<form>` tag — use `onClick` async handler. Disable all inputs + button while loading.**

---

### Page 3 — Messaging App (`/app`)

The authenticated core experience. Full gradient treatment throughout — no plain colors anywhere. The three-pane layout uses the co-dominant forest/parchment split: sidebar is forest-dominant, chat pane is parchment-dominant.

---

#### Layout

**Desktop:**
```
┌────────────────────┬───────────────────────────────┐
│  SIDEBAR           │  CHAT PANE                    │
│  bg-gradient-to-b  │  bg-gradient-to-br            │
│  from-forest-deep  │  from-parchment to-parchment  │
│  to-forest         │  -dim                         │
│                    │                               │
│  [BadrLink logo]   │  [Chat header]                │
│  [Search bar]      │  [Message list — scrollable]  │
│  [New convo btn]   │  [Message input bar]          │
│  [Conversation     │                               │
│   list items]      │                               │
└────────────────────┴───────────────────────────────┘
```

**Mobile:**
- Sidebar = full-screen conversation list (slide to chat pane on tap)
- Chat pane = full-screen message view with back arrow
- Transition: horizontal slide (`translateX`) between views

---

#### Sidebar

- Background: `bg-gradient-to-b from-[#1A3A20] to-forest` — never a flat color
- Width: `w-72 lg:w-80 flex-shrink-0`
- `border-r border-sage/10`

**Logo / App Header:**
- `"BadrLink"` gradient text `from-parchment to-sage`, display font
- Below: logged-in username in `text-sage/70 text-sm`
- Padding: `px-5 py-5 border-b border-parchment/10`

**Search Bar:**
- `bg-gradient-to-r from-forest-surface/60 to-fern/20 border border-sage/15 rounded-xl`
- `text-parchment placeholder-sage/50 text-sm px-4 py-2`
- Search icon left: `text-sage/60`
- Focus: `ring-1 ring-fern/40`
- `mx-4 my-3`

**New Conversation Button:**
- `bg-gradient-to-r from-fern to-sage text-parchment rounded-xl px-4 py-2 text-sm font-medium`
- `hover:scale-[1.02] hover:shadow-md hover:shadow-fern/20 active:scale-[0.98] transition-all`
- `+ New Chat` with a plus icon
- `mx-4 mb-3`

**Conversation List Items:**
- Each item: `flex items-center gap-3 px-4 py-3 cursor-pointer transition-all duration-200`
- Default bg: transparent
- Hover: `bg-gradient-to-r from-parchment/8 to-sage/5`
- Active: `bg-gradient-to-r from-parchment/15 to-sage/10 border-l-2 border-fern`
- Avatar: `w-10 h-10 rounded-full bg-gradient-to-br from-sage to-fern` (or from-fern to-forest for variety) — initials inside in parchment text
- Name: `text-parchment text-sm font-medium`
- Last message preview: `text-sage/60 text-xs truncate`
- Timestamp: `text-sage/50 text-xs ml-auto`
- Online indicator dot: `w-2 h-2 rounded-full bg-gradient-to-br from-sage to-fern` (absolute, bottom-right of avatar)

**Skeleton loading** (while fetching conversations):
- Animated `bg-gradient-to-r from-forest/40 via-fern/20 to-forest/40 bg-[length:200%_100%] animate-shimmer` bars
- Shimmer keyframe: `@keyframes shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }`

---

#### Chat Pane

- Background: `bg-gradient-to-br from-parchment to-parchment-dim` — warm, light, legible
- Flex column: header → message list (flex-1 overflow-y-auto) → input bar

**Chat Header:**
- `bg-gradient-to-r from-parchment-dim to-parchment border-b border-forest/15`
- `px-6 py-4 flex items-center gap-3`
- Avatar: `w-10 h-10 rounded-full bg-gradient-to-br from-fern to-forest`
- Conversation name: `text-forest text-base font-semibold` (display font)
- Participants / subtitle: `text-fern/70 text-xs`
- Online status dot (if applicable): `bg-gradient-to-br from-sage to-fern`

**Message List:**
- `flex-1 overflow-y-auto px-6 py-4 flex flex-col gap-3`
- Scroll to bottom on new message (smooth)
- Infinite scroll upward for history

**Message Bubbles:**

Sent (current user):
- Align right: `flex justify-end`
- Bubble: `bg-gradient-to-br from-fern to-forest text-parchment rounded-2xl rounded-br-sm px-4 py-2 max-w-[70%] text-sm`
- Timestamp: `text-parchment/50 text-[10px] mt-1 text-right`

Received (other user):
- Align left: `flex justify-start items-end gap-2`
- Avatar: `w-7 h-7 rounded-full bg-gradient-to-br from-sage to-fern flex-shrink-0`
- Bubble: `bg-gradient-to-br from-forest/15 to-fern/10 border border-forest/20 text-forest rounded-2xl rounded-bl-sm px-4 py-2 max-w-[70%] text-sm`
- Timestamp: `text-forest/40 text-[10px] mt-1`

**Skeleton messages** (while loading):
- Alternating left/right shimmer bars using the same shimmer animation as sidebar

**Empty state** (no conversation selected):
- Centered in chat pane: `/app/texting_image.png` + `"Select a conversation to start chatting"` in `text-forest/40` italic display font
- Illustration: `w-32 h-32 opacity-40`

**Message Input Bar:**
- `bg-gradient-to-r from-parchment-dim to-parchment border-t border-forest/10 px-4 py-4`
- Textarea: `flex-1 bg-gradient-to-r from-forest/8 to-fern/5 border border-forest/15 rounded-2xl text-forest placeholder-forest/40 px-4 py-3 text-sm resize-none focus:ring-2 focus:ring-fern/30 focus:outline-none transition-all`
- Placeholder: `"Type a message..."`
- Send button: `bg-gradient-to-br from-fern to-forest text-parchment rounded-xl p-3 hover:scale-[1.05] hover:shadow-md hover:shadow-fern/30 active:scale-[0.95] transition-all`
- Send icon SVG inside button
- Enter to send, Shift+Enter for newline

---

#### New Conversation Modal

Triggered by `"+ New Chat"` button:

- Backdrop: `fixed inset-0 bg-forest/60 backdrop-blur-sm z-50`
- Modal card: `bg-gradient-to-br from-forest to-forest-deep border border-sage/20 rounded-3xl p-6 max-w-sm w-full`
- Heading: `"New Conversation"` — gradient text parchment→sage
- Toggle: Private / Group — `bg-gradient-to-r from-fern/20 to-sage/10` active pill
- Input: username field (same styling as auth inputs)
- For Group: multi-select participant list + group name input
- Confirm button: full gradient fern→sage CTA
- Cancel: parchment/40 text link

---

#### Toast Notifications (WebSocket errors)

- Fixed `bottom-6 right-6 z-50`
- `bg-gradient-to-r from-forest/90 to-fern/70 backdrop-blur-sm border border-sage/20 text-parchment rounded-2xl px-5 py-3 shadow-xl`
- Slide-up + fade-in animation on appear, slide-down + fade-out on dismiss
- Auto-dismiss after 4 seconds

---

## Color Palette

One unified palette used **across all pages** — landing, auth, and app interior. The palette is built around two co-dominant anchors: **parchment** (warm beige) and **forest** (deep green), with fern and sage as midtone accents. Neither color dominates exclusively — they share equal visual weight, splitting large surfaces between them.

### The Four Tokens

| Token | Hex | Role |
|-------|-----|------|
| `--color-parchment` | `#E5D9B6` | **Co-dominant light anchor** — page backgrounds, right-side panels, message bubbles (received), form cards |
| `--color-sage` | `#A4BE7B` | **Accent / midtone** — hover states, tags, online indicators, secondary UI, dividers |
| `--color-fern` | `#5F8D4E` | **Action color** — buttons, CTAs, links, active conversation highlights, sent message bubbles |
| `--color-forest` | `#285430` | **Co-dominant dark anchor** — sidebar backgrounds, navbars, dark surfaces, headers |

### Dominant Color Strategy

> **Forest and parchment split the canvas.** The layout should feel like two panels of a painting — one rich dark green, one warm cream — held together by fern and sage accents. Avoid making one color 80%+ of the screen. Aim for roughly 45/45/10 between forest, parchment, and the accent pair.

Example surface mapping:
- Landing page: **parchment** background (`#E5D9B6`) with **forest** navbar — not dark-on-dark
- Auth card: **forest** card surface on a **parchment** page background — card floats as a dark panel against warm cream
- App sidebar: **forest** (`#285430`)
- App chat pane: **parchment** (`#E5D9B6`) as the main message area background
- Sent bubbles: **fern** (`#5F8D4E`) text in parchment, or fern fill
- Received bubbles: **forest** (`#285430`) fill with parchment text

### Full CSS Token Set

```css
:root {
  /* Core four */
  --color-parchment:      #E5D9B6;
  --color-parchment-dim:  #D4C89E;
  --color-parchment-deep: #C9BA8C;
  --color-sage:           #A4BE7B;
  --color-sage-muted:     #8FAD62;
  --color-fern:           #5F8D4E;
  --color-fern-dark:      #4A7040;
  --color-forest:         #285430;
  --color-forest-deep:    #1A3A20;

  /* Semantic — light surfaces (parchment-dominant contexts) */
  --color-bg-light:       #E5D9B6;   /* page background */
  --color-surface-light:  #D4C89E;   /* cards on light bg */
  --color-input-light:    #F0EAD6;   /* form inputs on light bg */

  /* Semantic — dark surfaces (forest-dominant contexts) */
  --color-bg-dark:        #285430;   /* sidebar, navbar, dark panels */
  --color-surface-dark:   #1A3A20;   /* elevated elements on dark bg */
  --color-input-dark:     #2E5E37;   /* form inputs on dark bg */

  /* Text */
  --color-text-on-light:  #285430;   /* forest text on parchment */
  --color-text-on-dark:   #E5D9B6;   /* parchment text on forest */
  --color-text-secondary: #A4BE7B;   /* sage for secondary/muted */
  --color-text-muted:     #7A9E67;

  /* Utility */
  --color-border-light:   rgba(40, 84, 48, 0.2);   /* forest border on parchment */
  --color-border-dark:    rgba(229, 217, 182, 0.15); /* parchment border on forest */
  --color-error:          #B83232;
  --color-online:         #A4BE7B;   /* sage dot for online status */
}
```

### Tailwind Config

```js
// tailwind.config.ts
theme: {
  extend: {
    colors: {
      parchment: {
        DEFAULT: '#E5D9B6',
        dim:     '#D4C89E',
        deep:    '#C9BA8C',
        light:   '#F0EAD6',
      },
      sage: {
        DEFAULT: '#A4BE7B',
        muted:   '#8FAD62',
      },
      fern: {
        DEFAULT: '#5F8D4E',
        dark:    '#4A7040',
      },
      forest: {
        DEFAULT: '#285430',
        deep:    '#1A3A20',
        surface: '#2E5E37',
      },
    },
    fontFamily: {
      display: ['YourDisplayFont', 'serif'],   // distinctive, non-generic
      body:    ['YourBodyFont', 'sans-serif'],
    },
  }
}
```

---

## Technology Stack

| Concern | Choice |
|---------|--------|
| Framework | Next.js (App Router) |
| Styling | TailwindCSS (mandatory) + CSS variables |
| Fonts | Choose distinctive, non-generic fonts from Google Fonts or Fontsource |
| Animations | Motion library (`motion/react`) for React components |
| WebSocket | STOMP over SockJS (`@stomp/stompjs` + `sockjs-client`) |
| State | React Context or Zustand |
| HTTP | `fetch` with a typed wrapper, or Axios |

### Tailwind Config Note
See the full config in the Color Palette section above. Remember to also register your font families there.

---

## Authentication (REST)

```
POST /api/auth/login
Body:     { "username": "string", "password": "string" }
Response: { "token": "<jwt>", "username": "...", "userId": 123 }
```

- Store token in **React context / Zustand** (avoid localStorage unless strong XSS defenses exist)
- Attach to all protected requests: `Authorization: Bearer <token>`
- Attach to WebSocket CONNECT frame as well

---

## REST Endpoints

### Conversations
```
GET  /api/conversations?cursor=<id>&limit=<n>
     → ConversationPageDTO { items: ConversationDTO[], nextCursor: id|null }

POST /api/conversations
     Body (PRIVATE): { "type": "PRIVATE", "targetUsername": "bob" }
     Body (GROUP):   { "type": "GROUP", "participantUsernames": ["a","b"], "name": "Team" }
     → ConversationDTO
     Errors: 400 / 404 / 409 with ErrorResponse
```

> ⚠️ **Do NOT send `creatorId`** — server derives it from the token.

### Messages
```
GET  /api/conversations/{id}/messages?cursor=<messageId>&limit=20
     → { items: MessageDTO[], nextCursor: id|null }
     Page size: 20 (server-enforced)

POST /api/conversations/{id}/messages
     Body: { "content": "..." }
     → MessageDTO
```

> ⚠️ **Do NOT send `senderId`** — server derives it from the token.

### Users
```
GET /api/users/me
GET /api/users/{id}
GET /api/users/username/{username}
```

---

## WebSocket (STOMP)

```
URL: ws(s)://<host>/ws
     or /ws-sockjs (if SockJS is configured)

CONNECT header:
  Authorization: Bearer <token>
```

### Subscriptions
| Destination | Purpose |
|-------------|---------|
| `/topic/conversations` | Broadcast: new conversation notifications |
| `/topic/conversations/{conversationId}` | Messages for an open conversation |
| `/user/queue/errors` | Per-user error notifications |

### Sending
- Send to `/app/chat.send` (verify server mapping)
- Payload: `{ "content": "..." }` — omit `senderId`

### Error Handling
- Listen on `/user/queue/errors` for `ErrorResponse` JSON
- Map `status` to behavior:
    - `401` → logout and redirect to `/login`
    - `429` → respect `Retry-After`, show backoff message
    - Others → show toast with `ErrorResponse.message`

---

## Data Transfer Objects (DTOs)

### ErrorResponse
```typescript
interface ErrorResponse {
  timestamp: string;      // ISO string
  status: number;
  error: string;
  message: string;
  path: string;
  errorCode: string | null;
}
```

### ConversationDTO
```typescript
interface ConversationDTO {
  conversationId: number;
  type: "PRIVATE" | "GROUP";
  name?: string;
  createdAt: string;      // ISO timestamp
  participants: UserDTO[];
}
```

### MessageDTO
```typescript
interface MessageDTO {
  messageId: number;
  conversationId: number;
  userId: number;
  content: string;
  createdAt: string;      // ISO timestamp
}
```

### CreateConversationRequest
```typescript
interface CreateConversationRequest {
  type: "PRIVATE" | "GROUP";
  targetUsername?: string;
  participantUsernames?: string[];
  name?: string;
}
```

---

## Pagination Semantics

- Cursor is a **Long (numeric ID)** — not a timestamp
- `nextCursor` = the last item ID in the returned page
- If `nextCursor === null` → no more items exist
- To load older messages: pass `cursor=<nextCursor>` in the next request
- Smaller IDs = older messages

---

## Error Handling & Retry

- On **429**: check `Retry-After` header, implement exponential backoff
- Always display `ErrorResponse.message` to the user in human-readable form
- Never expose raw stack traces or internal errors in the UI
- Implement a global error boundary in Next.js

---

## Component Architecture (Suggested)

```
/app
  /(public)
    /          → LandingPage
    /login     → AuthPage (login + register tabs)
  /(auth)
    /app       → MessagingLayout
      /        → ConversationList (sidebar)
      /[id]    → ChatPane

/components
  /ui          → Button, Input, Avatar, Toast, Skeleton, Modal
  /chat        → MessageBubble, MessageList, MessageInput
  /sidebar     → ConversationItem, NewConversationModal
  /layout      → AppShell, MobileSidebar, DesktopSidebar

/lib
  /api.ts      → typed REST client
  /stomp.ts    → STOMP client setup & hook
  /auth.ts     → token storage & context

/hooks
  useConversations.ts
  useMessages.ts
  useWebSocket.ts

/styles
  globals.css  → CSS variable definitions
  theme.ts     → Tailwind-side tokens
```

---

## Example Flows

### Login & Connect
```
1. POST /api/auth/login → receive { token, userId, username }
2. Store token in context
3. STOMP CONNECT with Authorization header
4. Subscribe to /user/queue/errors
5. Subscribe to /topic/conversations
6. Redirect to /app
```

### Create Private Conversation
```
1. POST /api/conversations { type: "PRIVATE", targetUsername: "bob" }
2. Server returns ConversationDTO
3. Add to conversation list
4. Subscribe to /topic/conversations/{conversationId}
5. Navigate to new conversation
```

### Send Message
```
1. POST /api/conversations/{id}/messages { content: "hey" }
   OR send via STOMP /app/chat.send
2. Real-time echo arrives via /topic/conversations/{id}
3. Append to message list, scroll to bottom
```

---

## Additional Frontend Notes

- **Never send** `creatorId` or `senderId` — server derives from token
- **Theme file**: Keep all color/font tokens in `theme.ts` and `globals.css` for one-place rebranding
- **Skeleton loading**: Use animated skeletons for conversation list and message list on initial load
- **Accessibility**: Maintain accessible contrast ratios, ARIA labels on icon buttons, keyboard navigability
- **Responsive breakpoints** (Tailwind defaults are fine):
    - `sm`: 640px — compact mobile
    - `md`: 768px — tablet / transition to sidebar layout
    - `lg`: 1024px — full three-pane desktop
- **Font loading**: Use `next/font` for zero-CLS font loading
- **Image optimization**: Use `next/image` for any avatars or assets