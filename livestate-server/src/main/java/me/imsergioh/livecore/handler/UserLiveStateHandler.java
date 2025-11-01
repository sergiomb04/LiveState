package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserLiveStateHandler implements LiveStateHandler<User> {

    @Getter
    private static final UserLiveStateHandler handler = new UserLiveStateHandler();

    @Override
    public String getWebSocketChannelName() {
        return "user/{userId}";
    }

    @Override
    @GetMapping("/api/user/{userId}")
    public User getData(@PathVariable Map<String, String> params) {
        String userId = params.get("userId");
        return UserService.get().getUserByName(userId);
    }
}
