package me.imsergioh.livecore.instance;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {

    private final String name;
    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
