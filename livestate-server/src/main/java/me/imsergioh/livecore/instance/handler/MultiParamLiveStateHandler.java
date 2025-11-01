package me.imsergioh.livecore.instance.handler;

import com.google.gson.Gson;
import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.manager.ClientsManager;
import me.imsergioh.livecore.service.AuthService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MultiParamLiveStateHandler<T> extends TextWebSocketHandler implements ILiveStateHandler<T> {

    private static final Gson gson = new Gson();

    // Map de parámetros -> Map<paramValueKey, sesiones>
    private final Map<String, Map<String, Set<WebSocketSession>>> sessionsMap = new ConcurrentHashMap<>();

    // Patrón de ruta, por ejemplo "/realtime/user/{userId}/group/{groupId}"
    protected abstract String getPathPattern();

    // Extraer datos según path, devuelve Map<paramName, paramValue>
    protected Map<String, String> extractParams(WebSocketSession session) {
        String[] patternParts = getPathPattern().split("/");
        String[] pathParts = session.getUri().getPath().split("/");

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < Math.min(patternParts.length, pathParts.length); i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                String key = patternParts[i].substring(1, patternParts[i].length() - 1);
                params.put(key, pathParts[i]);
            }
        }
        return params;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!hasPermission(session)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
            return;
        }

        // Register session
        Map<String, String> params = extractParams(session);
        String key = String.join("|", params.values());
        sessionsMap.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet())
                .add(session);
        ClientsManager.register(session);

        // Send initial data IF it's true at config
        if (MainConfig.sendInitDataOnConnectWebSocket()) {
            session.sendMessage(new TextMessage(gson.toJson(getData(params))));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Map<String, String> params = extractParams(session);
        String key = String.join("|", params.values());
        Map<String, Set<WebSocketSession>> map = sessionsMap.getOrDefault(key, new HashMap<>());
        map.getOrDefault(key, new HashSet<>()).remove(session);
        ClientsManager.unregister(session);
    }

    public void broadcastUpdate(Map<String, String> params) {
        try {
            String key = String.join("|", params.values());
            String json = gson.toJson(getData(params));
            for (WebSocketSession session : sessionsMap.getOrDefault(key, new HashMap<>())
                                                      .getOrDefault(key, new HashSet<>())) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public boolean hasPermission(WebSocketSession session) {
        if (!hasTokenAuth()) return true;
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query != null && query.startsWith("token=")) {
            return AuthService.isValidAdminToken(query.substring(6));
        }
        return false;
    }

    public abstract T getData(Map<String, String> params);

    @Override
    public T getData() {
        return null;
    }

    @Override
    public void broadcastUpdate() {}
}
