# LiveState ⚡

LiveState is a full-stack framework designed to elegantly synchronize state between a Spring Boot Java/Kotlin backend and a Next.js (React) frontend in real-time. It leverages WebSockets to push state updates from the server to connected clients seamlessly, using a custom React hook that behaves similarly to `useState`.

## Features ✨

- **Real-time State Synchronization**: Automatically pushes backend state changes to the frontend without manual polling.
- **Unified API & WebSocket Handlers**: Define your data fetching logic securely in one place, creating both a REST endpoint and a WebSocket channel simultaneously.
- **Familiar React Hooks**: The `useRealtimeState` hook allows developers to sync state effortlessly and manage multiple WebSocket subscriptions efficiently.
- **Built-in JWT Authentication**: Secure WebSocket channels using JWT tokens natively within the framework.
- **Automatic Reconnection & Batching**: The client automatically reconnects on network failures and batches frequent WebSocket messages to optimize React rendering performance.

## Architecture 🏛️

The mono-repo is split into two core components:
- `livestate-server`: A Spring Boot backend utilizing Java WebSockets to broadcast state.
- `livestate-nextjs`: A Next.js frontend with tailored React hooks for connecting to the server.

## Quick Start 🚀

### 1. Backend: Creating a LiveState Handler
Create a handler by implementing `LiveStateHandler<T>` to configure a WebSocket channel dynamically.

```java
@RestController
@WSChannelName("user/{userId}")
public class UserLiveStateHandler implements LiveStateHandler<User> {

    @Override
    @GetMapping("/api/user/{userId}")
    public User getData(@PathVariable Map<String, String> params) {
        String userId = params.get("userId");
        // Custom logic to fetch your data
        return UserService.get().getUserByName(userId);
    }
}
```
*Note: Any time the user data changes, you can simply call `broadcastUpdate(...)` on your handler to push the latest state to all subscribed clients!*

### 2. Frontend: Subscribing to State
On the Next.js frontend, use the `useRealtimeState` hook to subscribe to the configured channel and receive live updates.

```jsx
import { useRealtimeState } from "@/lib/realtime";

export default function UserProfile({ authToken, initialUserData, username }) {
  // Pass the channel name, initial data, and auth token
  const [userData, setUserData, connectionState] = useRealtimeState(
    `user/${username}`,
    initialUserData,
    authToken
  );

  return (
    <div>
      <p>Connection Status: {connectionState}</p>
      <h1>{userData.name}</h1>
    </div>
  );
}
```

## Running the Project ⚙️

### Backend
Navigate to `livestate-server` and run the Spring Boot application using Gradle:
```bash
cd livestate-server
./gradlew bootRun
```

### Frontend
Navigate to `livestate-nextjs` and run the Next.js development server:
```bash
cd livestate-nextjs
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.
