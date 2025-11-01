"use client";

import UserInfo from "@/components/UserInfo";
import publish from "@/lib/realtime";

import { useState } from "react";

export default function TestUnmount({ authToken, username, initialUserData }) {
  return (
    <>
      <UserInfo
        authToken={authToken}
        username={username}
        initialUserData={initialUserData}
      />

      <button
        className="p-4 bg-white text-black font-bold text-2xl"
        onClick={() =>
          publish("fakePlayer", { name: "Pepito", uuid: "BBB" })
        }
      >
        PUBLICAR
      </button>
    </>
  );
}
