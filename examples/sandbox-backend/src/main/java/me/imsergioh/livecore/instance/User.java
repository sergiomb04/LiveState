package me.imsergioh.livecore.instance;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.livecore.handler.ChannelsHandler;
import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.service.TokenAuthorizationService;
import me.imsergioh.livecore.service.UserService;
import me.imsergioh.livecore.util.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class User {

    private final String name;
    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
        sendUserUpdate();
    }

    public void sendUserUpdate() {
        // Send ONLY if canAccess: UserHandler
        LiveStateHandler<?> userHandler = UserLiveStateHandler.getHandler();
        Map<String, String> params = Map.of("userId", name);
        userHandler.forEachSubscribed(params, client -> {
            String token = client.getAuthToken();
            if (!TokenAuthorizationService.canAccessUser(token, name)) return;
            client.send("user/" + name, ChannelsHandler.getData(userHandler.getWebSocketChannelName(), params));
        });

        // Send broadcast simple: UsersHandler
        UsersLiveStateHandler.getHandler().broadcastUpdate();
    }

    public static Optional<User> getUser(LiveStateClient client) {
        String authToken = client.getAuthToken();
        if (authToken == null) return Optional.empty();
        var claims = JwtUtil.getClaims(authToken);
        String username = claims.getSubject();
        User user = UserService.get().getUserByName(username);
        if (user == null) return Optional.empty();
        return Optional.of(user);
    }

}
