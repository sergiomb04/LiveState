package me.imsergioh.livecore.config;

import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.instance.handler.ILiveStateHandler;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        register(registry,
                new UserLiveStateHandler(),
                new UsersLiveStateHandler()
        );
    }

    private static void register(WebSocketHandlerRegistry registry, WebSocketHandler... handlers) {
        for (WebSocketHandler handler : handlers) {
            if (ILiveStateHandler.class.isAssignableFrom(handler.getClass())) {
                ILiveStateHandler<?> liveStateHandler = (ILiveStateHandler<?>) handler;
                registry.addHandler(handler, liveStateHandler.getWSPaths()).setAllowedOrigins("*");
            }
        }
    }

}