import { useState, useEffect, useRef } from "react";

const listeners = {};
const sockets = {};
const states = {};

function connect(endpoint, token) {
  if (sockets[endpoint]) return;

  let url = `ws://localhost:8080/realtime/${endpoint}`;
  if (token) url += `?token=${encodeURIComponent(token)}`;

  let reconnectAttempts = 0;
  let batch = [];
  let batchTimeout;

  function createSocket() {
    states[endpoint] = "connecting";
    const ws = new WebSocket(url);

    ws.onopen = () => {
      states[endpoint] = "open";
      reconnectAttempts = 0;
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      batch.push(data);

      if (!batchTimeout) {
        batchTimeout = setTimeout(() => {
          if (listeners[endpoint]) {
            listeners[endpoint].forEach((cb) => cb(batch.length > 1 ? batch : batch[0]));
          }
          batch = [];
          batchTimeout = null;
        }, 50); // 50ms batch
      }
    };

    ws.onclose = () => {
      states[endpoint] = "closed";
      delete sockets[endpoint];

      // intentar reconectar
      const timeout = Math.min(1000 * 2 ** reconnectAttempts, 30000);
      setTimeout(() => {
        reconnectAttempts++;
        createSocket();
      }, timeout);
    };

    sockets[endpoint] = ws;
  }

  createSocket();
}

export function useRealtimeState(endpoint, initialValue, token) {
  const [state, setState] = useState(initialValue);
  const [connectionState, setConnectionState] = useState("closed");

  useEffect(() => {
    if (!listeners[endpoint]) listeners[endpoint] = [];
    listeners[endpoint].push(setState);

    connect(endpoint, token);

    const interval = setInterval(() => {
      setConnectionState(states[endpoint] || "closed");
    }, 100);

    return () => {
      listeners[endpoint] = listeners[endpoint].filter((cb) => cb !== setState);

      // si no hay listeners, cerrar socket
      if (listeners[endpoint].length === 0 && sockets[endpoint]) {
        sockets[endpoint].close();
        delete sockets[endpoint];
      }

      clearInterval(interval);
    };
  }, [endpoint, token]);

  return [state, setState, connectionState];
}

