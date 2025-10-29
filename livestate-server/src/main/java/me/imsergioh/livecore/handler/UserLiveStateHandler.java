package me.imsergioh.livecore.handler;

import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.MultiParamLiveStateHandler;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Component
@RestController
public class UserLiveStateHandler extends MultiParamLiveStateHandler<User> {

    @Override
    protected String getPathPattern() {
        return "/realtime/user/{userId}";
    }

    @Override
    @GetMapping("/api/user/{userId}")
    public User getData(@PathVariable Map<String, String> params) {
        String userId = params.get("userId");
        return UserService.get().getUserByName(userId);
    }
}
