package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.auth.AuthService;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Component
public class UsersLiveStateHandler extends LiveStateHandler<List<User>> {

    @Getter
    private static UsersLiveStateHandler handler;

    public UsersLiveStateHandler() {
        handler = this;
    }

    @Override
    public boolean hasPermission(WebSocketSession session) {
        if (session.getUri() == null) return false;
        String query = session.getUri().getQuery();
        System.out.println("QUERY "+query);
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            return AuthService.isValidAdminToken(token);
        }
        return false;
    }

    @Override
    public List<User> getData() {
        return UserService.get().getUsers();
    }
}
