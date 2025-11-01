package me.imsergioh.livecore.manager;

import com.google.gson.Gson;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.service.AuthService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ClientsManager extends TextWebSocketHandler {

    private static final Gson gson = new Gson();

    private static final Map<String, LiveStateClient> clients = new HashMap<>();

    public static void register(WebSocketSession session) {
        if (clients.containsKey(session.getId())) return;
        LiveStateClient client = new LiveStateClient(session);
        clients.put(session.getId(), client);
        client.onConnect();
    }

    public static void unregister(WebSocketSession session) {
        LiveStateClient client = get(session);
        if (client == null) return;
        client.onDisconnect();
        clients.remove(session.getId());
    }

    public static LiveStateClient get(WebSocketSession session) {
        return clients.get(session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Check permissions (Authorization GOD)
        if (!hasPermission(session)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
            return;
        }

        // Register session
        register(session);

        /* Send initial data IF it's true at config
        if (MainConfig.sendInitDataOnConnectWebSocket()) {
            session.sendMessage(new TextMessage(gson.toJson(getData())));
        }*/
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        try {
            Map<String, Object> object = gson.fromJson(payload, HashMap.class);
            ClientActionsManager.perform(get(session), object);
        } catch (Exception e) {
            // Disconnect if not valid payload
            unregister(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        unregister(session);
    }

    public boolean hasPermission(WebSocketSession session) {
        //if (!hasTokenAuth()) return true;

        // With token auth (URI)
        if (session.getUri() == null) return false;
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            return AuthService.isValidAdminToken(token);
        }
        return false;
    }

}
