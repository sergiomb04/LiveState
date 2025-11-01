package me.imsergioh.livecore.instance.connection;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class LiveStateClient {

    private final WebSocketSession session;

    public LiveStateClient(WebSocketSession session) {
        this.session = session;
    }

    public void onConnect() {

    }

    public void onDisconnect() {

    }

}
