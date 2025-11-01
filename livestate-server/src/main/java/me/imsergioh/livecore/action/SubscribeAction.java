package me.imsergioh.livecore.action;

import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;

import java.util.Map;

public class SubscribeAction implements IConnectionAction {

    @Override
    public String getName() {
        return "subscribe";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> objectMap) {
        System.out.println("SUB -> " + client.getSession().getId());
    }
}
