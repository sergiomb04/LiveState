package me.imsergioh.livecore.action;

import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.manager.ClientsManager;

import java.util.Map;

public class PublishAction implements IConnectionAction {

    @Override
    public String getName() {
        return "publish";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> objectMap) {
        if (!client.isAuth() && MainConfig.requiresAuth()) return;
        String channel = (String) objectMap.get("channel");
        Map<String, Object> data = (Map<String, Object>) objectMap.get("data");

        ClientsManager.broadcast(channel, data);
    }

}
