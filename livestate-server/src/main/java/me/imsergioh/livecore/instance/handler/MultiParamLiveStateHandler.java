package me.imsergioh.livecore.instance.handler;

import me.imsergioh.livecore.manager.ClientsManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

public abstract class MultiParamLiveStateHandler<T> extends TextWebSocketHandler implements ILiveStateHandler<T> {

    public void broadcastUpdate(Map<String, String> params) {
        ClientsManager.broadcast(getWebSocketChannelName(), getWebSocketChannelName(params));
    }

    public abstract T getData(Map<String, String> params);

    @Override
    public void broadcastUpdate() {}
}
