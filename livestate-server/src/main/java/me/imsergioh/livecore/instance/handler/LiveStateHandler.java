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

public abstract class LiveStateHandler<T> implements ILiveStateHandler<T> {

    private static final Gson gson = new Gson();

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
    public void broadcastUpdate() {
        /*try {
            String json = gson.toJson(getData());

            TODO: RECOBRAR VIDA -> ARREGLAR
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }*/
    }

}
