package me.imsergioh.livecore.service;

import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UserService {

    private static UserService service;

    @Getter
    private final List<User> users = new CopyOnWriteArrayList<>();

    public UserService() {
        service = this;
        users.add(new User(1, "Alice"));
        users.add(new User(2, "Bob"));
        users.add(new User(3, "Charlie"));
    }

    public static UserService get() {
        return service;
    }
}
