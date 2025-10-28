import { useState, useEffect } from 'react';

const sockets = {};
const listeners = {};
const buffer = {};
const BATCH_INTERVAL = 50;

function connect(endpoint) {
  if (!sockets[endpoint] || sockets[endpoint].readyState !== WebSocket.OPEN) {
    const ws = new WebSocket(`ws://localhost:8080/realtime/${endpoint}`);
    sockets[endpoint] = ws;
    buffer[endpoint] = [];

    ws.onmessage = (event) => {
      const newData = JSON.parse(event.data);
      buffer[endpoint].push(newData);
    };

    ws.onclose = () => {
      setTimeout(() => connect(endpoint), 1000);
    };

    setInterval(() => {
      if (buffer[endpoint].length > 0 && listeners[endpoint]) {
        const latest = buffer[endpoint][buffer[endpoint].length - 1];
        buffer[endpoint] = [];
        listeners[endpoint].forEach((cb) => cb(latest));
      }
    }, BATCH_INTERVAL);
  }
}

export function useRealtimeState(endpoint, initialValue) {
  const [state, setState] = useState(initialValue);

  useEffect(() => {
    if (!listeners[endpoint]) listeners[endpoint] = [];
    listeners[endpoint].push(setState);

    connect(endpoint);

    return () => {
      listeners[endpoint] = listeners[endpoint].filter((cb) => cb !== setState);
    };
  }, [endpoint]);

  return [state, setState];
}