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

}
