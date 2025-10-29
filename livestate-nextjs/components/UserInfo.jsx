"use client"

import { useRealtimeState } from '@/lib/realtime';

export default function UserInfo({ authToken, username, initialUserData }) {
  //const [userData] = useRealtimeState(`user/${username}`, initialUserData, authToken);

  return (
    <p>{JSON.stringify(initialUserData)}</p>
  );
}