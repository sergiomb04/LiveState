"use client"

import { sendAction, useRealtimeState } from 'livestate-nextjs';

export default function FakePlayersList({ authToken, initialData }) {
    const [data] = useRealtimeState('fakePlayers', initialData, authToken);
    const players = Array.isArray(data) ? data : [];

    const handleRemove = (uuid) => {
        sendAction("removeFakePlayer", { uuid });
    };

    return (
        <section className="w-full max-w-2xl rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
            <header className="mb-4 flex items-center justify-between">
                <div>
                    <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
                        Fake Players
                    </h2>
                    <p className="text-sm text-zinc-500 dark:text-zinc-400">
                        {players.length} listed
                    </p>
                </div>

                <span className="rounded-full bg-emerald-100 px-2 py-0.5 text-xs font-medium text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300">
                    Live
                </span>
            </header>

            {players.length === 0 ? (
                <p className="text-sm text-zinc-500 dark:text-zinc-400">
                    No fake players listed.
                </p>
            ) : (
                <div className="space-y-3">
                    {players.map((player) => (
                        <div
                            key={player.uuid}
                            className="rounded-xl border border-zinc-200 bg-zinc-50 p-3 dark:border-zinc-700 dark:bg-zinc-800/60"
                        >
                            <div className="flex items-center gap-3">
                                <div className="inline-flex h-10 w-10 items-center justify-center rounded-full bg-indigo-100 text-sm font-bold text-indigo-700 dark:bg-indigo-900/40 dark:text-indigo-300">
                                    {player.name?.charAt(0).toUpperCase() ?? "?"}
                                </div>

                                <div className="flex-1">
                                    <p className="font-semibold text-zinc-900 dark:text-zinc-100">
                                        {player.name}
                                    </p>

                                    <p
                                        className="truncate font-mono text-xs text-zinc-500 dark:text-zinc-400"
                                        title={player.uuid}
                                    >
                                        {player.uuid}
                                    </p>
                                </div>

                                <button
                                    onClick={() => handleRemove(player.uuid)}
                                    className="rounded-lg bg-red-500/10 px-3 py-1 text-xs font-semibold text-red-600 hover:bg-red-500/20 dark:text-red-400"
                                >
                                    Remove
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </section>
    );
}
