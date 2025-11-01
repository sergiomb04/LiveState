package me.imsergioh.livecore.instance.connection;

import com.google.gson.Gson;
import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.manager.ClientsManager;
import me.imsergioh.livecore.service.UserService;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Getter
public class LiveStateClient {

    private static final Gson gson = new Gson();

    private final WebSocketSession session;
    private final String authToken;
    private User user;

    private final Set<String> subscriptions = new HashSet<>();

    public LiveStateClient(WebSocketSession session) {
        this.session = session;
        this.authToken = getAuthToken(session);
    }

    public void onConnect() {
        System.out.println("New web client connected -> " + session.getId());
        System.out.println("TOKEN " + authToken);
    }

    public void onDisconnect() {
        System.out.println("New web client disconnected -> " + session.getId());
    }

    public void subscribe(String channel) {
        subscriptions.add(channel);
        System.out.println("SUB " + channel);
    }

    public void unsubscribe(String channel) {
        subscriptions.remove(channel);
        System.out.println("UNSUB " + channel);
    }

    public boolean isSubscribed(String channel) {
        return subscriptions.contains(channel);
    }

    public void send(String channel, Object object) {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("channel", channel);
        wrapper.put("payload", object);

        String json = gson.toJson(wrapper);
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            e.printStackTrace(System.out);
            ClientsManager.unregister(session);
        }
    }

    public Optional<User> getUser() {
        if (user != null) return Optional.of(user);
        if (authToken == null) return Optional.empty();
        var claims = JwtUtil.getClaims(authToken);
        String username = claims.getSubject();
        user = UserService.get().getUserByName(username);
        if (user == null) return Optional.empty();
        return Optional.of(user);
    }

    private static String getAuthToken(WebSocketSession session) {
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query != null && query.startsWith("token=")) {
            return query.substring(6);
        }
        return null;
    }

}
