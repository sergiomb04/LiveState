package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UsersLiveStateHandler extends LiveStateHandler<List<User>> {

    @Getter
    private static final UsersLiveStateHandler handler = new UsersLiveStateHandler();

    @Override
    public String getWebSocketChannelName() {
        return "users";
    }

    @Override
    @GetMapping("/api/users")
    public List<User> getData(Map<String, String> params) {
        return UserService.get().getUsers();
    }
}
