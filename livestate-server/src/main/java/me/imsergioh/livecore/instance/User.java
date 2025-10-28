package me.imsergioh.livecore.instance;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
