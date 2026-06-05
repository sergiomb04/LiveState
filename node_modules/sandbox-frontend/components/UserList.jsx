"use client"

import { useRealtimeState } from 'livestate-nextjs';

export default function UserList({ authToken, initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers, authToken);

  return (
    <ul>
      {users.map((user) => (
        <li key={user.name}>{user.name} - {user.score}</li>
      ))}
    </ul>
  );
}
