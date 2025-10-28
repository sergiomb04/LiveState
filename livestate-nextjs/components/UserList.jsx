"use client"

import { useRealtimeState } from '@/lib/realtime';

export default function UserList({ initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers);

  return (
    <ul>
      {users.map((user) => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}