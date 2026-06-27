package me.imsergioh.livecore.instance;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
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

    private final int age;

    public User(String name, int score, int age) {
        this.name = name;
        this.score = score;
        this.age = age;
    }

    public void setScore(int score) {
        this.score = score;
        sendUserUpdate();
    }

    public void sendUserUpdate() {
        // UserHandler: Send ONLY if canAccess
        UserLiveStateHandler.getHandler()
                .broadcastUpdateIf(Map.of("userId", name),
                client -> TokenAuthorizationService.canAccessUser(client.getAuthToken(), name));

        // UsersHandler: Send broadcast simple
        //UsersLiveStateHandler.getHandler().broadcastUpdate();
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
