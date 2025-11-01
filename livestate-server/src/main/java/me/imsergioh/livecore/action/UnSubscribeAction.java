package me.imsergioh.livecore.action;

import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;

import java.util.Map;

public class UnSubscribeAction implements IConnectionAction {

    @Override
    public String getName() {
        return "unsubscribe";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> objectMap) {
        if (!client.isAuth()) return;
        String channel = (String) objectMap.get("sub");
        client.unsubscribe(channel);
    }
}
