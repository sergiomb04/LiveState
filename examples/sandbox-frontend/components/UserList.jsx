"use client"

import { useLayoutEffect, useRef } from 'react';
import { useRealtimeState } from 'livestate-nextjs';

export default function UserList({ authToken, initialUsers }) {
  const [users] = useRealtimeState('users', initialUsers, authToken);
  const itemRefs = useRef(new Map());
  const previousTops = useRef(new Map());

  useLayoutEffect(() => {
    const nextTops = new Map();

    users.forEach((user) => {
      const element = itemRefs.current.get(user.name);
      if (!element) return;

      const currentTop = element.getBoundingClientRect().top;
      nextTops.set(user.name, currentTop);

      const previousTop = previousTops.current.get(user.name);
      if (previousTop === undefined) return;

      const deltaY = previousTop - currentTop;
      if (deltaY === 0) return;

      element.style.transform = `translateY(${deltaY}px)`;
      element.style.transition = 'transform 0s';

      requestAnimationFrame(() => {
        element.style.transform = 'translateY(0)';
        element.style.transition = 'transform 320ms cubic-bezier(0.22, 1, 0.36, 1)';
      });
    });

    previousTops.current = nextTops;
  }, [users]);

  return (
    <section className="w-full max-w-md rounded-2xl border border-black/10 bg-white/80 p-4 shadow-sm backdrop-blur dark:border-white/15 dark:bg-zinc-900/70">
      <header className="mb-3 flex items-center justify-between">
        <h2 className="text-sm font-semibold uppercase tracking-wide text-zinc-600 dark:text-zinc-300">
          Live users
        </h2>
        <span className="rounded-full bg-zinc-100 px-2 py-0.5 text-xs font-medium text-zinc-700 dark:bg-zinc-800 dark:text-zinc-200">
          {users.length}
        </span>
      </header>

      {users.length === 0 ? (
        <p className="rounded-xl border border-dashed border-zinc-300 px-3 py-4 text-center text-sm text-zinc-500 dark:border-zinc-700 dark:text-zinc-400">
          No hay usuarios conectados.
        </p>
      ) : (
        <ul className="space-y-2">
          {users.map((user, index) => (
            <li
              key={user.name}
              ref={(element) => {
                if (element) {
                  itemRefs.current.set(user.name, element);
                } else {
                  itemRefs.current.delete(user.name);
                }
              }}
              className="flex items-center justify-between rounded-xl border border-zinc-200 bg-zinc-50 px-3 py-2 transition-colors hover:bg-zinc-100 dark:border-zinc-700 dark:bg-zinc-800/60 dark:hover:bg-zinc-800"
            >
              <div className="flex items-center gap-2">
                <span className="inline-flex h-6 w-6 items-center justify-center rounded-full bg-zinc-200 text-xs font-semibold text-zinc-700 dark:bg-zinc-700 dark:text-zinc-100">
                  {index + 1}
                </span>
                <span className="font-medium text-zinc-900 dark:text-zinc-100">{user.name}</span>
              </div>
              <span className="rounded-md bg-emerald-100 px-2 py-1 text-xs font-semibold text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-300">
                {user.score} points
              </span>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
