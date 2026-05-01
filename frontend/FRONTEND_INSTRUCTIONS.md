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

The public face of the product. Should feel distinctive and intentional, not like a generic SaaS template.

**Required sections:**
- Hero — strong headline, short tagline, CTA buttons (Get Started, Login)
- Features — 3–4 key features with iconography or illustration
- Footer — minimal, with links

**Design notes:**
- The hero should use rich background treatment (gradient mesh, layered botanical patterns, or geometric texture)
- Typography should lead — use a large, expressive display font
- Staggered entrance animations on page load
- Mobile: stack everything vertically; CTA should be full-width
- Desktop: allow asymmetric layouts, overlapping elements, or bold negative space

---

### Page 2 — Login / Register (`/login`)

A focused, distraction-free auth experience that still carries the product's personality.

**Required elements:**
- Toggle between Login and Register tabs/views
- Login form: username + password fields + submit
- Register form: username + password + confirm password fields + submit
- Animated transition between forms
- Error state handling (inline field errors using `ErrorResponse.message`)
- "Back to home" link

**Design notes:**
- Consider a split-screen layout on desktop (decorative/brand half + form half)
- Form inputs should feel polished — custom focus rings, smooth transitions
- Mobile: full-screen single-column
- Show loading spinner on submit; disable form during request

**API:**
```
POST /api/auth/login
Body: { "username": "string", "password": "string" }
Response 200: { "token": "<jwt>", "username": "...", "userId": 123, ... }
```
- On success: store token in React context (not localStorage unless XSS defenses are in place), redirect to `/app`
- On error: display `ErrorResponse.message` below the form

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

The app uses a **warm botanical palette** — earthy, grounded, and distinctive. It evokes natural depth without being dull. All tokens must be defined as CSS/Tailwind variables.

| Token | Hex | Usage |
|-------|-----|-------|
| `--color-parchment` | `#E5D9B6` | Light backgrounds, message bubbles (received), cards |
| `--color-sage` | `#A4BE7B` | Secondary accents, tags, online indicators, hover states |
| `--color-fern` | `#5F8D4E` | Primary actions, buttons, links, active states |
| `--color-forest` | `#285430` | Dark primary, sidebar background, dominant dark surface |

**Extended tokens to derive:**
```css
:root {
  --color-parchment:   #E5D9B6;
  --color-parchment-dim: #D4C89E;
  --color-sage:        #A4BE7B;
  --color-sage-muted:  #8FAD62;
  --color-fern:        #5F8D4E;
  --color-fern-dark:   #4A7040;
  --color-forest:      #285430;
  --color-forest-deep: #1A3A20;

  /* Semantic */
  --color-bg:          #1A3A20;      /* dark page background */
  --color-surface:     #285430;      /* cards, panels */
  --color-surface-2:   #2E5E37;      /* elevated surfaces */
  --color-text-primary: #E5D9B6;
  --color-text-secondary: #A4BE7B;
  --color-text-muted:  #7A9E67;
  --color-accent:      #A4BE7B;
  --color-cta:         #5F8D4E;
  --color-cta-hover:   #4A7040;
  --color-error:       #C0392B;
  --color-border:      rgba(164, 190, 123, 0.2);
}
```

**Theme guidance:**
- Dark theme is the default (forest + parchment text)
- Consider a light mode toggle where parchment becomes the background and forest becomes text
- Avoid introducing colors outside this system — instead derive tints/shades and use opacity
- Message bubbles: sent = fern background, received = parchment/forest surface

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
Extend Tailwind's config to include the palette tokens:
```js
// tailwind.config.ts
theme: {
  extend: {
    colors: {
      parchment: '#E5D9B6',
      sage:      '#A4BE7B',
      fern:      '#5F8D4E',
      forest:    '#285430',
    },
    fontFamily: {
      // Add your chosen display and body fonts here
      display: ['YourDisplayFont', 'serif'],
      body:    ['YourBodyFont', 'sans-serif'],
    }
  }
}
```

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