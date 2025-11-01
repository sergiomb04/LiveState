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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class LiveStateHandler<T> extends TextWebSocketHandler implements ILiveStateHandler<T> {

    private static final Gson gson = new Gson();

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public boolean hasPermission(WebSocketSession session) {
        if (!hasTokenAuth()) return true;

        // With token auth (URI)
        if (session.getUri() == null) return false;
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            return AuthService.isValidAdminToken(token);
        }
        return false;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Check permissions (Authorization GOD)
        if (!hasPermission(session)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
            return;
        }

        // Register session
        sessions.add(session);
        ClientsManager.register(session);

        // Send initial data IF it's true at config
        if (MainConfig.sendInitDataOnConnectWebSocket()) {
            session.sendMessage(new TextMessage(gson.toJson(getData())));
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        ClientsManager.unregister(session);
    }

    @Override
    public void broadcastUpdate() {
        try {
            String json = gson.toJson(getData());
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

}
