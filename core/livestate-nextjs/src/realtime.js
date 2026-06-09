"use client";

import { useState, useEffect } from "react";

const socketManager = {
  ws: null,
  connectionState: "closed",
  subscriptions: {},
  pending: [],
  token: null,
};

export function connect(token) {
  if (socketManager.ws) return;

  socketManager.token = token;
  let url = `ws://localhost:8080/realtime`;

  let reconnectAttempts = 0;
  let batch = [];
  let batchTimeout;

  function createSocket() {
    socketManager.connectionState = "connecting";
    const ws = new WebSocket(url);

    ws.onopen = () => {
      socketManager.connectionState = "open";
      reconnectAttempts = 0;

      if (token) {
        sendAction("auth", {token});
      }

      Object.keys(socketManager.subscriptions).forEach((subKey) => {
        sendAction("subscribe", {sub: subKey})
      });

      socketManager.pending.forEach((sub) =>
        sendAction("subscribe", {sub})
      );
      socketManager.pending = [];
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      const key = data.channel;

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
        }, 50);
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

export function sendAction(actionName, data = {}) {
  socketManager.ws.send(
    JSON.stringify({
      action: actionName,
      ...data,
    })
  );
}

export default function publish(channel, data) {
  sendAction("publish", {channel, data});
}

export function useRealtimeState(key, initialValue, token) {
  const [state, setState] = useState(initialValue);
  const [connectionState, setConnectionState] = useState(socketManager.connectionState);

  useEffect(() => {
    if (!socketManager.subscriptions[key]) socketManager.subscriptions[key] = new Set();
    socketManager.subscriptions[key].add(setState);

    connect(token);

    if (socketManager.connectionState === "open") {
      sendAction("subscribe", {sub: key});
    } else {
      socketManager.pending.push(key);
    }

    const interval = setInterval(() => setConnectionState(socketManager.connectionState), 100);

    return () => {
      socketManager.subscriptions[key].delete(setState);
      if (socketManager.subscriptions[key].size === 0) {
        delete socketManager.subscriptions[key];
        if (socketManager.ws && socketManager.connectionState === "open") {
          sendAction("unsubscribe", {sub: key});
        }
      }
      clearInterval(interval);
    };
  }, [key, token]);

  return [state, setState, connectionState];
}