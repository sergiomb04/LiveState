package me.imsergioh.livecore.handler;

import me.imsergioh.livecore.instance.handler.LiveStateHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

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

    public static void registerChannelInterceptor(String name, Consumer<Map<String, Object>> consumer) {
        channelsHandler.put(name, new LiveStateHandler<>() {
            final UUID uuid = UUID.randomUUID();
            @Override
            public Object getData(Map<String, String> params) {
                return Map.of("uuid", uuid);
            }
            @Override
            public void onMessage(Map<String, Object> payload) {
                consumer.accept(payload);
            }
        });
    }

    public static void perform(String name, Map<String, Object> payload) {
        LiveStateHandler<?> handler = channelsHandler.get(name);
        if (handler == null) return;
        handler.onMessage(payload);
    }

}
