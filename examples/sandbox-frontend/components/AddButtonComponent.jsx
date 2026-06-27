"use client";

import { publish } from "livestate-nextjs";
import { useState } from "react";

export default function AddButtonComponent() {

  const [name, setName] = useState("");

  const handlePublish = () => {
    publish("fakePlayer", { name: "Pepito", uuid: crypto.randomUUID() });
  };

  return (
    <section className="w-full max-w-md rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
      <header className="mb-3">
        <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
          Add new Fake Player
        </h2>
      </header>
      
      <div className="flex flex-col gap-2">
        <input type="text"
        className="bg-gray-700 p-2 rounded-xl"/>

      <button
        type="button"
        className="w-full bg-blue-500 py-2 rounded-xl hover:bg-blue-400 transition-colors duration-250"
        value={name}
        onChange={(e) => setName(e.target.value)}
        onClick={handlePublish}
      >
        Add
      </button>
      </div>
    </section>
  );
}
