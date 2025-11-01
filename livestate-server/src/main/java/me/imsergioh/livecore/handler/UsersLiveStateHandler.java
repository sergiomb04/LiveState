package me.imsergioh.livecore.handler;

import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.instance.handler.WSHandlerPaths;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Component
@RestController
@WSHandlerPaths(paths = "/realtime/users")
public class UsersLiveStateHandler extends LiveStateHandler<List<User>> {

    @Override
    @GetMapping("/api/users")
    public List<User> getData() {
        return UserService.get().getUsers();
    }
}
