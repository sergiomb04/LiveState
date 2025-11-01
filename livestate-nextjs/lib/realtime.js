import { useState, useEffect } from "react";

const socketManager = {
  ws: null,
  connectionState: "closed",
  subscriptions: {},   // { key: Set(callbacks) }
  pending: [],
  token: null,
};

function connect(token) {
  if (socketManager.ws) return;

  socketManager.token = token;
  let url = `ws://localhost:8080/realtime`;
  if (token) url += `?token=${encodeURIComponent(token)}`;

  let reconnectAttempts = 0;
  let batch = [];
  let batchTimeout;

  function createSocket() {
    socketManager.connectionState = "connecting";
    const ws = new WebSocket(url);

    ws.onopen = () => {
      socketManager.connectionState = "open";
      reconnectAttempts = 0;

      // reenviar las suscripciones pendientes
      socketManager.pending.forEach((sub) => ws.send(JSON.stringify({ action: "subscribe", sub })));
      socketManager.pending = [];
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      const key = data.subscriptionKey;

      if (socketManager.subscriptions[key]) {
        batch.push({ key, data: data.payload });
      }

      if (!batchTimeout) {
        batchTimeout = setTimeout(() => {
          batch.forEach(({ key, data }) => {
            socketManager.subscriptions[key].forEach((cb) => cb(data));
          });
          batch = [];
          batchTimeout = null;
        }, 50); // batch 50ms
      }
    };

    ws.onclose = () => {
      socketManager.connectionState = "closed";
      socketManager.ws = null;
      const timeout = Math.min(1000 * 2 ** reconnectAttempts, 30000);
      setTimeout(() => {
        reconnectAttempts++;
        createSocket();
      }, timeout);
    };

    socketManager.ws = ws;
  }

  createSocket();
}

export function useRealtimeState(key, initialValue, token) {
  const [state, setState] = useState(initialValue);
  const [connectionState, setConnectionState] = useState(socketManager.connectionState);

  useEffect(() => {
    // iniciar suscripción
    if (!socketManager.subscriptions[key]) socketManager.subscriptions[key] = new Set();
    socketManager.subscriptions[key].add(setState);

    connect(token);

    // si la conexión ya está abierta, enviar suscripción al backend
    if (socketManager.connectionState === "open") {
      socketManager.ws.send(JSON.stringify({ action: "subscribe", sub: key }));
    } else {
      socketManager.pending.push(key);
    }

    const interval = setInterval(() => setConnectionState(socketManager.connectionState), 100);

    return () => {
      // desuscribir
      socketManager.subscriptions[key].delete(setState);
      if (socketManager.subscriptions[key].size === 0) {
        delete socketManager.subscriptions[key];
        if (socketManager.ws) {
          socketManager.ws.send(JSON.stringify({ action: "unsubscribe", sub: key }));
        }
      }
      clearInterval(interval);
    };
  }, [key, token]);

  return [state, setState, connectionState];
}