package me.imsergioh.livecore.manager;

import me.imsergioh.livecore.instance.connection.LiveStateClient;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class ClientsManager {

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

}
