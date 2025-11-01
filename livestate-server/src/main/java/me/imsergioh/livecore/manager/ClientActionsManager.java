package me.imsergioh.livecore.manager;

import me.imsergioh.livecore.action.SubscribeAction;
import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;

import java.util.HashMap;
import java.util.Map;

public class ClientActionsManager {

    private static final Map<String, IConnectionAction> actions = new HashMap<>();

    public static void init() {
        register(new SubscribeAction());
    }

    public static void perform(LiveStateClient client, Map<String, Object> objectMap) {
        String name = (String) objectMap.get("action");
        IConnectionAction action = actions.get(name);
        if (action == null) return;
        action.onAction(client, objectMap);
    }

    public static void register(IConnectionAction... actionsToRegister) {
        for (IConnectionAction action : actionsToRegister) {
            actions.put(action.getName(), action);
        }
    }

}
