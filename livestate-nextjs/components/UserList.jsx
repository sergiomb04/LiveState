"use client"

import { useRealtimeState } from '@/lib/realtime';
import { useEffect } from 'react';

export default function UserList({ initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers);

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