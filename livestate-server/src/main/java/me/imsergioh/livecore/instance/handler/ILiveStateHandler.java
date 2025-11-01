package me.imsergioh.livecore.instance.handler;

import java.util.Map;

public interface ILiveStateHandler<T> {

    String getWebSocketChannelName();

    default String getWebSocketChannelName(Map<String, String> params) {
        String channel = getWebSocketChannelName();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            channel = channel.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return channel;
    }

    void broadcastUpdate();
    T getData(Map<String, String> params);
}
