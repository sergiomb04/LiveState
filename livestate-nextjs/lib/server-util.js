export async function fetchData(endpoint, { authToken, defaultValue = [], options = {} } = {}) {
  try {
    const headers = authToken
      ? { Authorization: `Bearer ${authToken}`, ...options.headers }
      : options.headers;

    const response = await fetch(`http://localhost:8080${endpoint}`, {
      ...options,
      headers,
      cache: "no-store",
    });

    if (!response.ok) return defaultValue;
    return await response.json();
  } catch {
    return defaultValue;
  }
}