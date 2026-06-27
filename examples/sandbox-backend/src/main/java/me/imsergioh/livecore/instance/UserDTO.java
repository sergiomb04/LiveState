package me.imsergioh.livecore.instance;

import lombok.Getter;

@Getter
public class UserDTO {

    private final String name;
    private final int score;

    public UserDTO(User user) {
        this.name = user.getName();
        this.score = user.getScore();
    }

}
