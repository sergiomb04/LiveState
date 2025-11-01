package me.imsergioh.livecore.instance;

import lombok.Getter;
import lombok.Setter;
import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;

import java.util.Map;

@Getter @Setter
public class User {

    private final String name;
    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
        sendUserUpdate();
    }

    public void sendUserUpdate() {
        UserLiveStateHandler.getHandler().broadcastUpdate(Map.of("userId", name));
        UsersLiveStateHandler.getHandler().broadcastUpdate();
    }

}
