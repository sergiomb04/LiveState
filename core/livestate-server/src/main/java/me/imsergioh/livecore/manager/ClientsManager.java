package me.imsergioh.livecore.manager;

import com.google.gson.Gson;

import me.imsergioh.livecore.handler.ChannelsHandler;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.util.ChannelUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ClientsManager extends TextWebSocketHandler {

    private static final Gson gson = new Gson();

    private static final Map<String, LiveStateClient> clients = new ConcurrentHashMap<>();

    private static final Set<Consumer<LiveStateClient>> connectActions = new HashSet<>();
    private static final Set<Consumer<LiveStateClient>> disconnectActions = new HashSet<>();

    public static void addConnectionAction(Consumer<LiveStateClient> action) {
        connectActions.add(action);
    }

    public static void addDisconnectionAction(Consumer<LiveStateClient> action) {
        disconnectActions.add(action);
    }

    public static void forEachClient(Consumer<LiveStateClient> consumer) {
        new HashSet<>(clients.values()).forEach(consumer::accept);
    }

    public static void forEachSubscribed(String channel, Consumer<LiveStateClient> consumer) {
        new HashSet<>(clients.values()).forEach(client -> {
            if (!client.isSubscribed(channel)) return;
            consumer.accept(client);
        });
    }

    public static void broadcast(String channelNamePattern, String channel) {
        new HashSet<>(clients.values()).forEach(client -> {
            if (!client.isSubscribed(channel)) return;
            Map<String, String> params = ChannelUtil.extractParams(channelNamePattern, channel);
            client.send(channel, ChannelsHandler.getData(channelNamePattern, params));
        });
    }

    public static void broadcast(String channel, Map<String, Object> objectMap) {
        new HashSet<>(clients.values()).forEach(client -> {
            if (!client.isSubscribed(channel)) return;
            client.send(channel, objectMap);
        });
    }

    public static void registerConnection(WebSocketSession session) {
        if (clients.containsKey(session.getId())) return;
        LiveStateClient client = new LiveStateClient(session);
        clients.put(session.getId(), client);
        client.onConnect();
        connectActions.forEach(action -> action.accept(client));
    }

    public static void unregisterConnection(WebSocketSession session) {
        unregisterConnection(session, null);
    }

    public static void unregisterConnection(WebSocketSession session, Exception e) {
        if (e != null) {
            e.printStackTrace(System.out);
        }
        LiveStateClient client = get(session);
        if (client == null) return;
        client.onDisconnect();
        clients.remove(session.getId());
        disconnectActions.forEach(action -> action.accept(client));
    }

    public static LiveStateClient get(WebSocketSession session) {
        return get(session.getId());
    }

    public static LiveStateClient get(String id) {
        return clients.get(id);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        registerConnection(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        try {
            Map<String, Object> object = gson.fromJson(payload, HashMap.class);
            ClientActionsManager.perform(get(session), object);
        } catch (Exception e) {
            // Disconnect if not valid payload
            unregisterConnection(session, e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        unregisterConnection(session);
    }
}
