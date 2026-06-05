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
        className="inline-flex w-full items-center justify-center rounded-xl bg-linear-to-r from-sky-600 to-cyan-600 px-4 py-3 text-sm font-semibold uppercase tracking-wide text-white shadow-lg shadow-cyan-900/20 transition-transform duration-150 hover:-translate-y-0.5 hover:from-sky-500 hover:to-cyan-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-cyan-400 focus-visible:ring-offset-2 focus-visible:ring-offset-white active:translate-y-0 dark:focus-visible:ring-offset-zinc-900"
        onClick={handlePublish}
      >
        Publish now
      </button>
    </section>
  );
}
