package me.imsergioh.livecore.instance.connection;

import com.google.gson.Gson;
import lombok.Getter;
import me.imsergioh.livecore.manager.ClientsManager;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class LiveStateClient {

    private static final Map<String, Set<String>> subscriptions = new ConcurrentHashMap<>();

    private static final Gson gson = new Gson();

    private final WebSocketSession session;
    @Getter
    private String authToken;

    public LiveStateClient(WebSocketSession session) {
        this.session = session;
    }

    public void setToken(String token) {
        authToken = token;
    }

    public void onConnect() {
        System.out.println("New web client connected -> " + session.getId());
    }

    public void onDisconnect() {
        System.out.println("New web client disconnected -> " + session.getId());
    }

    public void subscribe(String channel) {
        Set<String> set = getSubs(channel);
        set.add(session.getId());
        subscriptions.put(channel, set);
    }

    public void unsubscribe(String channel) {
        Set<String> set = getSubs(channel);
        set.remove(session.getId());
        subscriptions.put(channel, set);
        clearIfChannelIsEmpty(channel);
    }

    public boolean isSubscribed(String channel) {
        return getSubs(channel).contains(session.getId());
    }

    public void send(String channel, Object object) {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("channel", channel);
        wrapper.put("payload", object);

        String json = gson.toJson(wrapper);
        try {
            synchronized (session) {
                try {
                    session.sendMessage(new TextMessage(json));
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    ClientsManager.unregister(session);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            ClientsManager.unregister(session);
        }
    }

    public boolean isAuth() {
        return authToken != null;
    }

    private void removeChannelSubscriptions() {
        subscriptions.keySet().forEach(this::unsubscribe);
    }

    private static Set<String> getSubs(String channel) {
        return subscriptions.getOrDefault(channel, new HashSet<>());
    }

    private static void clearIfChannelIsEmpty(String channel) {
        Set<String> clients = getSubs(channel);
        if (!clients.isEmpty()) return;
        subscriptions.remove(channel);
    }

}
