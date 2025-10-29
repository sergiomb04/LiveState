package me.imsergioh.livecore.config;

import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final UserLiveStateHandler userHandler;
    private final UsersLiveStateHandler usersHandler;

    public WebSocketConfig(UserLiveStateHandler userHandler, UsersLiveStateHandler usersHandler) {
        this.userHandler = userHandler;
        this.usersHandler = usersHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(usersHandler, "/realtime/users").setAllowedOrigins("*");
        registry.addHandler(userHandler, "/realtime/user/*").setAllowedOrigins("*");
    }
}