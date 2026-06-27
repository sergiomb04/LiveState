package me.imsergioh.livecore.manager;

import me.imsergioh.livecore.action.AuthAction;
import me.imsergioh.livecore.action.PublishAction;
import me.imsergioh.livecore.action.SubscribeAction;
import me.imsergioh.livecore.action.UnSubscribeAction;
import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientActionsManager {

    private static final Map<String, IConnectionAction> actions = new HashMap<>();

    public static void init() {
        register(
                new AuthAction(),
                new SubscribeAction(),
                new UnSubscribeAction(),
                new PublishAction()
        );
    }

    public static void perform(LiveStateClient client, Map<String, Object> objectMap) {
        String name = (String) objectMap.get("action");
        IConnectionAction action = actions.get(name);
        if (action == null)
            return;
        if (MainConfig.requiresAuth() && action.requiresAuth() && !client.isAuth())
            return;
        action.onAction(client, objectMap);
    }

    public static void register(IConnectionAction... actionsToRegister) {
        for (IConnectionAction action : actionsToRegister) {
            actions.put(action.getName(), action);
        }
    }

    public static void register(String name, Consumer<Map<String, Object>> payloadConsumer) {
        actions.put(name, new IConnectionAction() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void onAction(LiveStateClient client, Map<String, Object> payload) {
                payloadConsumer.accept(payload);
            }
        });
    }

}
