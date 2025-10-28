"use client"

import { useRealtimeState } from '@/lib/realtime';

export default function UserList({ authToken, initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers, authToken);

  return (
    <ul>
      {users.map((user) => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}