package me.imsergioh.livecore.instance.handler;

import me.imsergioh.livecore.manager.ClientsManager;

public abstract class LiveStateHandler<T> implements ILiveStateHandler<T> {

    @Override
    public void broadcastUpdate() {
        ClientsManager.broadcast(getWebSocketChannelName(), getWebSocketChannelName());
    }

}
