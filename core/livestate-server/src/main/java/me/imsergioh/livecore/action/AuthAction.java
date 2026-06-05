package me.imsergioh.livecore.action;

import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.manager.ClientsManager;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class AuthAction implements IConnectionAction {

    @Override
    public String getName() {
        return "auth";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> objectMap) {
        String token = (String) objectMap.get("token");
        boolean valid = JwtUtil.validateToken(token);
        if (!valid) {
            disconnectInvalidAuthToken(client.getSession());
            return;
        }
        client.setToken(token);
    }

    public static void disconnectInvalidAuthToken(WebSocketSession session) {
        ClientsManager.unregister(session, new Exception("No valid auth token!"));
    }

}
