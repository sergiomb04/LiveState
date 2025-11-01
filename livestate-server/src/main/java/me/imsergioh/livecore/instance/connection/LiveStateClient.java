package me.imsergioh.livecore.instance.connection;

import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.service.UserService;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Getter
public class LiveStateClient {

    private final WebSocketSession session;
    private final String authToken;
    private User user;

    public LiveStateClient(WebSocketSession session) {
        this.session = session;
        this.authToken = getAuthToken(session);
    }

    public void onConnect() {
        System.out.println("New web client connected -> " + session.getId());
    }

    public void onDisconnect() {
        System.out.println("New web client disconnected -> " + session.getId());
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
