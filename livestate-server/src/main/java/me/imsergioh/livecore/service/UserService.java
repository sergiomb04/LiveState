package me.imsergioh.livecore.service;

import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.instance.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserLiveStateHandler userHandler;

    private static UserService service;

    private final Map<String, User> usersMap = new HashMap<>();

    public UserService() {
        service = this;

        register(
                new User("Alice", 11),
                new User("Bob", 22),
                new User("Charlie", 77),
                new User("Sergio", 999)
        );
    }

    public void sendUpdate(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        userHandler.broadcastUpdate(params);
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
