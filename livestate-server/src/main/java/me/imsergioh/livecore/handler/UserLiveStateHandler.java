package me.imsergioh.livecore.handler;

import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.MultiParamLiveStateHandler;
import me.imsergioh.livecore.instance.handler.ProtectedTokenHandler;
import me.imsergioh.livecore.instance.handler.WSHandlerPaths;
import me.imsergioh.livecore.service.TokenAuthorizationService;
import me.imsergioh.livecore.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Component
@ProtectedTokenHandler
@RestController
@WSHandlerPaths(paths = "/realtime/user/*")
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

    @Override
    public boolean hasPermission(WebSocketSession session) {
        if (!hasTokenAuth()) return true;

        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            Map<String, String> params = extractParams(session);
            return TokenAuthorizationService.canAccessUser(token, params.get("userId"));
        }
        return false;
    }
}
