import { useState, useEffect } from "react";

const listeners = {};
const sockets = {};

function connect(endpoint, token) {
  if (sockets[endpoint]) return;

  let url = `ws://localhost:8080/realtime/${endpoint}`;
  if (token) {
    url += `?token=${encodeURIComponent(token)}`;
  }

  const ws = new WebSocket(url);

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    if (listeners[endpoint]) {
      listeners[endpoint].forEach((cb) => cb(data));
    }
  };

  ws.onclose = () => {
    delete sockets[endpoint];
  };

  sockets[endpoint] = ws;
}

export function useRealtimeState(endpoint, initialValue, token) {
  const [state, setState] = useState(initialValue);

  useEffect(() => {
    if (!listeners[endpoint]) listeners[endpoint] = [];
    listeners[endpoint].push(setState);

    connect(endpoint, token);

    return () => {
      listeners[endpoint] = listeners[endpoint].filter((cb) => cb !== setState);
    };
  }, [endpoint, token]);

  return [state, setState];
}