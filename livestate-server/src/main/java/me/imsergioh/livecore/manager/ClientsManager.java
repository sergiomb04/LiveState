package me.imsergioh.livecore.manager;

import com.google.gson.Gson;
import me.imsergioh.livecore.LiveStateBackendApplication;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.util.ChannelUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ClientsManager extends TextWebSocketHandler {

    private static final Gson gson = new Gson();

    private static final Map<String, LiveStateClient> clients = new HashMap<>();

    public static void broadcast(String channelNamePattern, String channel) {
        clients.values().forEach(client -> {
            if (!client.isSubscribed(channel)) return;
            Map<String, String> params = ChannelUtil.extractParams(channelNamePattern, channel);
            client.send(channel, LiveStateBackendApplication.getData(channelNamePattern, params));
        });
    }

    public static void register(WebSocketSession session) {
        if (clients.containsKey(session.getId())) return;
        LiveStateClient client = new LiveStateClient(session);
        clients.put(session.getId(), client);
        client.onConnect();
    }

    public static void unregister(WebSocketSession session) {
        unregister(session, null);
    }

    public static void unregister(WebSocketSession session, Exception e) {
        if (e != null) {
            e.printStackTrace(System.out);
        }
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
        register(session);
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
}
