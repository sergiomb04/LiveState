"use client"

import { useRealtimeState } from 'livestate-nextjs';

export default function FakePlayer({ authToken, initialData }) {
    const [data] = useRealtimeState('fakePlayer', initialData, authToken);
    const safeData = data ?? {};
    const displayName = safeData.name || 'Unknown Player';
    const displayUuid = safeData.uuid || 'N/A';

    return (
        <section className="w-full max-w-md rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
            <header className="mb-4 flex items-center justify-between">
                <div className="flex items-center gap-3">
                    <div className="inline-flex h-10 w-10 items-center justify-center rounded-full bg-indigo-100 text-sm font-bold text-indigo-700 dark:bg-indigo-900/40 dark:text-indigo-300">
                        {String(displayName).charAt(0).toUpperCase()}
                    </div>
                    <div>
                        <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
                            Fake player
                        </h2>
                        <p className="text-base font-semibold text-zinc-900 dark:text-zinc-100">{displayName}</p>
                    </div>
        </div>
                <span className="rounded-full bg-emerald-100 px-2 py-0.5 text-xs font-medium text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300">
                    Live
                </span>
            </header>

            <dl className="grid grid-cols-1 gap-2">
                <div className="rounded-xl border border-zinc-200 bg-zinc-50 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-800/60">
                    <dt className="text-xs uppercase tracking-wide text-zinc-500 dark:text-zinc-400">Name</dt>
                    <dd className="text-sm font-semibold text-zinc-900 dark:text-zinc-100">{displayName}</dd>
                </div>

                <div className="rounded-xl border border-zinc-200 bg-zinc-50 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-800/60">
                    <dt className="text-xs uppercase tracking-wide text-zinc-500 dark:text-zinc-400">UUID</dt>
                    <dd className="truncate font-mono text-xs text-zinc-800 dark:text-zinc-200" title={displayUuid}>
                        {displayUuid}
                    </dd>
                </div>
            </dl>
        </section>
    );
}
