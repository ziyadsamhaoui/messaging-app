# Agent Guidelines for Messaging-application

This document provides essential knowledge for AI coding agents to be immediately productive in the `Messaging-application` codebase.

## 1. Big Picture Architecture

The application consists of a Java Spring Boot backend and a Next.js (App Router) frontend. They communicate via REST APIs and STOMP WebSockets.

- **Frontend (`frontend/`)**: Next.js, TailwindCSS, Motion library, STOMP over SockJS.
- **Backend (`src/main/java/messagerie/application/`)**: Java Spring Boot.

### Data Flow & Communication

- **Authentication**: REST `POST /api/auth/login` returns a JWT token. This token is used for subsequent REST requests (`Authorization: Bearer <token>`) and for STOMP WebSocket `CONNECT` frames.
- **REST Endpoints**:
    - Conversations: `GET /api/conversations`, `POST /api/conversations`
    - Messages: `GET /api/conversations/{id}/messages`, `POST /api/conversations/{id}/messages`
    - Users: `GET /api/users/me`, `GET /api/users/{id}`, `GET /api/users/username/{username}`
- **WebSocket (STOMP)**:
    - URL: `ws(s)://<host>/ws` or `/ws-sockjs`
    - Subscriptions:
        - `/topic/conversations`: New conversation notifications.
        - `/topic/conversations/{conversationId}`: Messages for an open conversation.
        - `/user/queue/errors`: Per-user error notifications.
    - Sending: `SEND /app/chat.send` with payload `{ "content": "..." }`.
- **Pagination**: Cursor-based using numeric IDs. `nextCursor` indicates the last item ID in the returned page. `null` means no more items. Smaller IDs are older messages.

## 2. Critical Developer Workflows

### Frontend

- **Requirements**: Node.js 18+ (20+ recommended), Backend running.
- **Environment**: Create `frontend/.env.local`:
    ```
    NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
    NEXT_PUBLIC_WS_URL=http://localhost:8000/ws
    ```
- **Run**:
    ```powershell
    npm install
    npm run dev
    ```
    Access at `http://localhost:3000`.
- **Scripts**:
    - `npm run check:env`: Quick environment validation.
    - `npm run lint`: Linting.
    - `npm run build`: Build for production.

### Backend

- **Build**: The project uses Maven.
    ```powershell
    ./mvnw clean install
    ```
- **Run**:
    ```powershell
    ./mvnw spring-boot:run
    ```
- **Docker**: `compose.yaml` is present for Docker Compose setup.

## 3. Project-Specific Conventions and Patterns

### Frontend

- **Design Philosophy**: Avoid "AI slop" aesthetic. Focus on unique typography, cohesive botanical-inspired color themes, impactful motion (using Motion library), and atmospheric backgrounds.
- **Color Palette**: Two co-dominant anchors: `parchment` (#E5D9B6) and `forest` (#285430), with `fern` (#5F8D4E) and `sage` (#A4BE7B) as midtone accents. Defined in `frontend/styles/globals.css` and `frontend/tailwind.config.ts`.
- **Styling**: TailwindCSS is mandatory. CSS variables are used for consistency.
- **Authentication**: Tokens stored in React context/Zustand (not localStorage).
- **Error Handling**:
    - Listen on `/user/queue/errors` for `ErrorResponse` JSON.
    - `401` status: logout and redirect to `/login`.
    - `429` status: respect `Retry-After` header, implement exponential backoff.
    - Others: show toast with `ErrorResponse.message`.
    - Global error boundary in Next.js.
- **Component Architecture**:
    - `app/`: Pages (`/`, `/login`, `/app`).
    - `components/ui`: Reusable UI components (Button, Input, Avatar, etc.).
    - `components/chat`: Chat-specific components (MessageBubble, MessageList, MessageInput).
    - `components/sidebar`: Sidebar components (ConversationItem, NewConversationModal).
    - `lib/`: API client, STOMP client, auth context.
    - `hooks/`: Data and WebSocket hooks.
- **Form Handling**: Use React `useState` for form state. No `<form>` tag; use `onClick` async handlers. Disable inputs/buttons while loading.
- **Responsive Design**: Mobile-first approach. Tailwind defaults for `sm`, `md`, `lg` breakpoints.
- **Image Optimization**: Use `next/image`.
- **Font Loading**: Use `next/font`.

## 4. Integration Points and External Dependencies

### Frontend

- **Dependencies**: `@stomp/stompjs`, `sockjs-client`, `motion/react`.
- **Backend Integration**: Relies heavily on the Java Spring Boot backend for all data operations and real-time communication.

### Backend

- **Dependencies**: Spring Boot, likely Spring Web, Spring Data, Spring Security, and WebSocket dependencies for STOMP.
- **Database**: Configuration likely in `src/main/resources/application.properties`.

## 5. Key Files and Directories

- `frontend/`: Next.js frontend application.
    - `frontend/app/`: Next.js pages.
    - `frontend/components/`: UI components.
    - `frontend/lib/`: API, STOMP, Auth logic.
    - `frontend/hooks/`: React hooks.
    - `frontend/styles/globals.css`: Global CSS and color variables.
    - `frontend/tailwind.config.ts`: Tailwind CSS configuration.
    - `frontend/.env.local`: Local environment variables.
- `src/main/java/messagerie/application/`: Java Spring Boot backend source code.
    - `src/main/java/messagerie/application/controller/`: REST controllers.
    - `src/main/java/messagerie/application/service/`: Business logic.
    - `src/main/java/messagerie/application/entity/`: JPA entities.
    - `src/main/java/messagerie/application/config/`: Spring configurations (e.g., WebSocket, Security).
    - `src/main/resources/application.properties`: Backend configuration.
- `pom.xml`: Maven project file for backend dependencies and build.
- `compose.yaml`: Docker Compose configuration.

