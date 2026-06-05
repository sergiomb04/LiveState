"use client"

import { useRealtimeState } from 'livestate-nextjs';

export default function UserInfo({ authToken, username, initialUserData }) {
  const [userData] = useRealtimeState(`user/${username}`, initialUserData, authToken);
  const safeData = userData ?? {};
  const primitiveEntries = Object.entries(safeData).filter(([, value]) => {
    const valueType = typeof value;
    return value !== null && (valueType === 'string' || valueType === 'number' || valueType === 'boolean');
  });

  return (
    <section className="w-full max-w-md rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
      <header className="mb-4 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="inline-flex h-10 w-10 items-center justify-center rounded-full bg-blue-100 text-sm font-bold text-blue-700 dark:bg-blue-900/40 dark:text-blue-300">
            {(username?.[0] || 'U').toUpperCase()}
          </div>
          <div>
            <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
              User info
            </h2>
            <p className="text-base font-semibold text-zinc-900 dark:text-zinc-100">{username}</p>
          </div>
        </div>
        <span className="rounded-full bg-emerald-100 px-2 py-0.5 text-xs font-medium text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300">
          Live
        </span>
      </header>

      {primitiveEntries.length > 0 ? (
        <dl className="mb-4 grid grid-cols-1 gap-2">
          {primitiveEntries.map(([key, value]) => (
            <div
              key={key}
              className="rounded-xl border border-zinc-200 bg-zinc-50 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-800/60"
            >
              <dt className="text-xs uppercase tracking-wide text-zinc-500 dark:text-zinc-400">{key}</dt>
              <dd className="text-sm font-semibold text-zinc-900 dark:text-zinc-100">{String(value)}</dd>
            </div>
          ))}
        </dl>
      ) : (
        <p className="mb-4 rounded-xl border border-dashed border-zinc-300 px-3 py-4 text-center text-sm text-zinc-500 dark:border-zinc-700 dark:text-zinc-400">
          No hay campos simples para mostrar.
        </p>
      )}
    </section>
  );
}
