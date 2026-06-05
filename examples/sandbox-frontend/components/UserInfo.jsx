"use client"

import { useRealtimeState } from 'livestate-nextjs';

export default function UserInfo({ authToken, username, initialUserData }) {
  const [userData] = useRealtimeState(`user/${username}`, initialUserData, authToken);

  return (
    <p>{JSON.stringify(userData)}</p>
  );
}
