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
        System.out.println("New web client connected -> " + session.getId());
    }

    public void onDisconnect() {
        System.out.println("New web client disconnected -> " + session.getId());
    }

}
