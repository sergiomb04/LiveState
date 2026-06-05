package me.imsergioh.livecore.instance.handler;

import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.manager.ClientsManager;

import java.util.Map;
import java.util.function.Consumer;

public interface LiveStateHandler<T> {

    default String getWebSocketChannelName() {
        return getClass().getDeclaredAnnotation(WSChannelName.class).value();
    }

    default String getWebSocketChannelName(Map<String, String> params) {
        String channel = getWebSocketChannelName();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            channel = channel.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return channel;
    }

    default void broadcastUpdate() {
        broadcastUpdate(null);
    }

    default void broadcastUpdate(Map<String, String> params) {
        String channelName = params == null ? getWebSocketChannelName() : getWebSocketChannelName(params);
        ClientsManager.broadcast(getWebSocketChannelName(), channelName);
    }

    default void forEachSubscribed(Map<String, String> params, Consumer<LiveStateClient> consumer) {
        String channelName = params == null ? getWebSocketChannelName() : getWebSocketChannelName(params);
        ClientsManager.forEachSubscribed(channelName, consumer);
    }

    T getData(Map<String, String> params);
}
