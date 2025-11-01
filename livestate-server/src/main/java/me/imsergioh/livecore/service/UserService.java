package me.imsergioh.livecore.service;

import me.imsergioh.livecore.instance.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final UserService service = new UserService();

    private final Map<String, User> usersMap = new HashMap<>();

    public UserService() {
        register(
                new User("Alice", 11),
                new User("Bob", 22),
                new User("Charlie", 77),
                new User("Sergio", 999)
        );
    }


    public User getUserByName(String userName) {
        return usersMap.get(userName);
    }

    private void register(User... users) {
        for (User user : users) {
            usersMap.put(user.getName(), user);
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(usersMap.values());
    }

    public static UserService get() {
        return service;
    }
}
