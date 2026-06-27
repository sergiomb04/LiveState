package me.imsergioh.livecore.handler;

import me.imsergioh.livecore.instance.handler.LiveStateHandler;

import java.util.HashMap;
import java.util.Map;

public class ChannelsHandler {

    private static final Map<String, LiveStateHandler<?>> channelsHandler = new HashMap<>();

    public static Object getData(String channelName, Map<String, String> params) {
        LiveStateHandler<?> handler = channelsHandler.get(channelName);
        return handler.getData(params);
    }

    public static void registerHandler(LiveStateHandler<?>... handlers) {
        for (LiveStateHandler<?> handler : handlers) {
            channelsHandler.put(handler.getWebSocketChannelName(), handler);
        }
    }

    public static void perform(String name, Map<String, Object> payload) {
        LiveStateHandler<?> handler = channelsHandler.get(name);
        if (handler == null) return;
        handler.onMessage(payload);
    }

}
