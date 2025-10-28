package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.auth.AuthService;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.instance.handler.ProtectedTokenHandler;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Component
@ProtectedTokenHandler
@RestController
public class UsersLiveStateHandler extends LiveStateHandler<List<User>> {

    @Getter
    private static UsersLiveStateHandler handler;

    public UsersLiveStateHandler() {
        handler = this;
    }

    @Override
    @GetMapping("/api/users")
    public List<User> getData() {
        return UserService.get().getUsers();
    }
}
