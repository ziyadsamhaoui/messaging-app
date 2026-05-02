# Messaging App — Frontend Instructions

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

**Interpret creatively. Make choices that feel genuinely designed for this specific product.** Vary between light and dark themes, different type hierarchies, different spatial compositions.

---

## App Overview & Pages

This is a **real-time messaging web app** targeting both mobile and desktop. It must be fully responsive from the start (mobile-first). **TailwindCSS is mandatory** for layout and utility classes.

### Pages to Build

| Page | Route | Auth Required |
|------|-------|--------------|
| Landing Page | `/` | No |
| Login / Register | `/login` or `/auth` | No |
| Messaging App | `/app` | Yes |

---

### Page 1 — Landing Page (`/`)

A single-screen, full-viewport landing page. **No scrolling sections, no feature grids, no footer prose** — just a clean, immediate first view that converts. The palette is co-dominant: parchment backgrounds split against a forest navbar and dark left panel.

**Layout (Desktop):**
```
┌─────────────────────────────────────────────────────────────┐
│  [Logo — forest text]            [Sign up]  [Log in]        │  ← Navbar: parchment bg, forest text
├──────────────────────────┬──────────────────────────────────┤
│  forest-bg left panel    │  parchment-bg right panel        │
│                          │                                  │
│  Large display headline  │   Floating chat UI mockup        │
│  (parchment text)        │   (message bubbles, avatars,     │
│                          │    conversational preview)       │
│  Short tagline           │   bubbles: fern (sent) +         │
│  (sage text)             │   forest (received)              │
│                          │                                  │
│  [Get Started →]         │                                  │
│  [Why AppName?]          │                                  │
│                          │                                  │
│  Fast. Private. Simple.  │                                  │
└──────────────────────────┴──────────────────────────────────┘
```

**Layout (Mobile):**
- Full-width parchment page
- Forest navbar top
- Headline and CTA stacked, forest text on parchment bg
- Chat mockup below CTA, full-width scaled card
- Three stat pills at bottom

**Required elements:**
- Navbar: forest background, parchment logo text, "Sign up" (fern filled button) + "Log in" (parchment outlined)
- Left panel: forest (`#285430`) with large parchment display-font headline (2–3 lines)
- Tagline: sage colored, `text-base` / `text-lg`, 2 lines max
- Two CTA buttons: "Get started →" (fern fill, parchment text) + "Why [AppName]?" (transparent, sage border)
- Three micro-stats row: e.g. "Fast enough. / Private. / Simple." — sage muted text
- Right panel: parchment (`#E5D9B6`) background with floating chat mockup card
- Chat mockup: static decorative HTML showing 3–4 sample message bubbles — sent (fern bg) and received (forest bg), avatar circles in sage

**Design notes:**
- The left/right split IS the palette — don't fight it, lean into it
- Navbar sits above the split as a single forest bar
- Chat mockup on right should feel elevated: subtle forest drop shadow, slight `rotate-1` or card border in `sage/20`
- Staggered entrance animations: headline words fade+slide in from left, mockup slides in from right
- No orange/pink gradients — the fern button is the only CTA accent needed
- Navbar border-bottom: `border-b border-parchment/10`

---

### Page 2 — Login / Register (`/login`)

A modern, split-personality auth page. The card sits centered on a **parchment** background, but the card itself is **forest** — a dark green panel floating on warm cream. This creates immediate visual contrast and reinforces the co-dominant palette. The heading adapts per mode: **"Welcome Back"** for login, **"Create Your Account"** for register.

**Layout:**
```
┌─────────────────────────────────────────────────────────────┐
│  [Logo — forest text]          parchment page background    │  ← top-left, links back to /
├─────────────────────────────────────────────────────────────┤
│                                                             │
│              ┌───────────────────────────┐                  │
│              │  forest surface card      │                  │
│              │                           │                  │
│              │  Welcome Back             │  ← login mode   │
│              │  Good to see you again.   │                  │
│              │  ─────────────────────    │                  │
│              │  Create Your Account      │  ← register mode│
│              │  Join and start chatting. │                  │
│              │                           │                  │
│              │  [Username input   👤]    │                  │
│              │  [Password input   👁]    │                  │
│              │  [████ strength bar]      │  register only  │
│              │                           │                  │
│              │  [  Log In / Sign Up  ]   │  ← fern button  │
│              │                           │                  │
│              │  Don't have an account?   │                  │
│              │  Create one →             │  ← sage link    │
│              └───────────────────────────┘                  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Required elements:**
- Page background: parchment (`#E5D9B6`)
- App logo top-left (outside card), forest color, links to `/`
- Centered card: forest surface (`#285430`), `rounded-2xl`, parchment-tinted shadow (`shadow-[0_8px_40px_rgba(40,84,48,0.25)]`)
- **Welcome heading:** `"Welcome Back"` (login) / `"Create Your Account"` (register) — large, parchment text, display font
- **Subline:** `"Good to see you again."` (login) / `"Join and start chatting."` (register) — sage color, smaller weight
- Thin sage divider line below the subline
- Input fields: `bg-forest-surface` (`#2E5E37`) background, parchment text, sage placeholder text, fern focus ring
    - Trailing icon on the right (user icon, eye-toggle for password) — sage colored
- Password strength bar (register only): 4 parchment-dim segments that fill with sage → fern → fern-dark as strength grows, labeled "Weak" / "Fair" / "Strong"
- Primary CTA button: full-width, fern fill (`#5F8D4E`), parchment text, `hover:bg-fern-dark`, smooth scale on press
- Footer toggle: "Don't have an account? **Create one →**" (login) / "Already have one? **Log in →**" (register) — muted parchment-dim text, sage colored link
- Animated crossfade / slide transition when switching between modes
- Show spinner inside CTA on submit; disable fields during request
- Error messages: small red text (`--color-error`) below the relevant input

**Design notes:**
- The contrast of the dark forest card on the warm parchment page is the visual statement — don't soften it
- No Google OAuth button (keep it clean unless explicitly needed)
- Mobile: card fills viewport with `mx-4`, slightly less padding
- The welcome message is the personality — make the font large and the subline feel warm, not clinical
- Input fields on the dark card should use a slightly lighter forest tone so they're distinguishable from the card background
- Avoid white anywhere on this page — stay strictly within the four-color system

---

### Page 3 — Messaging App (`/app`)

The core experience. Three-pane layout on desktop, drawer/modal navigation on mobile.

**Layout:**
```
Desktop:
┌────────────────┬────────────────────────────┬──────────────┐
│  Sidebar       │  Chat Pane                 │  Detail Pane │
│  (convos list) │  (messages + input)        │  (optional)  │
└────────────────┴────────────────────────────┴──────────────┘

Mobile:
- Sidebar = full screen (conversation list)
- Tap conversation → slide to full-screen chat pane
- Detail pane = bottom sheet or separate screen
```

**Sidebar features:**
- Search bar (filter conversations by name)
- Conversation list with skeleton loading
- New conversation button (opens modal: choose PRIVATE or GROUP)
- Active conversation highlighted
- Show participant names and last message preview (if available)

**Chat pane features:**
- Message list with infinite scroll (load older messages on scroll up)
- Skeleton placeholders while loading
- Message bubbles: distinguish sent vs received
- Timestamps on messages
- Auto-scroll to bottom on new message
- Message input: textarea + send button (support Enter to send, Shift+Enter for newline)
- Show conversation name / participants in header

**Real-time:**
- Subscribe to `/topic/conversations/{conversationId}` on open
- Append incoming `MessageDTO` to message list in real-time
- Subscribe to `/topic/conversations` for new conversation notifications
- Subscribe to `/user/queue/errors` on connect → show toast on error

**Error/feedback:**
- Toast notifications for WebSocket errors
- Retry with backoff on 429 responses (check `Retry-After` header)
- Empty states: "No conversations yet", "Say something..."

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