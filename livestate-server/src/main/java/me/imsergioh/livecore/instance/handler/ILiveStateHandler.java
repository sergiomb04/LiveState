package me.imsergioh.livecore.instance.handler;

import org.springframework.web.socket.WebSocketSession;

public interface ILiveStateHandler<T> {

    default boolean hasPermission(WebSocketSession session) {
        return true;
    }

    void broadcastUpdate();
    T getData();

    default boolean hasTokenAuth() {
        Class<?> clazz = this.getClass();
        return clazz.isAnnotationPresent(ProtectedTokenHandler.class);
    }

}
