package me.imsergioh.livecore.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class UserWebSocketHandler extends TextWebSocketHandler {

    @Getter
    private static UserWebSocketHandler handler;

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();

    public UserWebSocketHandler() {
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Actualizar nombre aleatoriamente (Poner un * pues pa ver si llega al front a tiempo real)
                List<User> users = getUsers();
                int idx = random.nextInt(users.size());
                users.get(idx).setName(users.get(idx).getName() + "*");
                broadcastUpdate();
            }
        }, 3000, 3000);
        handler = this;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        List<User> users = getUsers();
        session.sendMessage(new TextMessage(mapper.writeValueAsString(users))); // Enviar estado actúal al conectar (GOD)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    private void broadcastUpdate() {
        try {
            String json = mapper.writeValueAsString(getUsers());
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private List<User> getUsers() {
        return UserService.get().getUsers();
    }

}
