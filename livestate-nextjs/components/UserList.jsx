"use client"

import { useRealtimeState } from '@/lib/realtime';
import { useEffect } from 'react';

export default function UserList({ authToken, initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers, authToken);

  useEffect(() => {
    console.log("USERS:", users)
  }, [users])

  return (
    <ul>
      {users.map((user) => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}