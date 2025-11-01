"use client"

import UserInfo from '@/components/UserInfo'

import { useState } from "react";

export default function TestUnmount({authToken, username, initialUserData}) {
    const [showUser, setShowUser] = useState(true);
    return (
        <>
        
        {showUser && (
      <UserInfo
        authToken={authToken}
        username={username}
        initialUserData={initialUserData}
      />
      )}
        <button onClick={() => setShowUser(false)}>Desmontar UserInfo</button>
        </>
    )
}