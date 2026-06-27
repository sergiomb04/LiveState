"use client";

import { publish } from "livestate-nextjs";

export default function PublishButton() {
  const handlePublish = () => {
    publish("fakePlayer", { name: "Pepito", uuid: crypto.randomUUID() });
  };

  return (
    <section className="w-full max-w-md rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
      <header className="mb-3">
        <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
          Publish event
        </h2>
        <p className="mt-1 text-sm text-zinc-500 dark:text-zinc-400">
          Send a fakePlayer with name and random UUID.
        </p>
      </header>

      <button
        type="button"
        className="w-full bg-blue-500 py-2 rounded-xl hover:bg-blue-400 transition-colors duration-250"
        onClick={handlePublish}
      >
        Publish now
      </button>
    </section>
  );
}
