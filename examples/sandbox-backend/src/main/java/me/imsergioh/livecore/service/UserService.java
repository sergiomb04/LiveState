package me.imsergioh.livecore.service;

import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.UserDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private static final UserService service = new UserService();

    private final Map<String, User> usersMap = new ConcurrentHashMap<>();

    public UserService() {
        register(
                new User("Alice", 11, 18),
                new User("Bob", 22, 43),
                new User("Charlie", 77, 27),
                new User("Sergio", 999, 22)
        );
    }

    public void startSimulatingRealtimeScores() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                usersMap.forEach((name, user) -> {
                    int variation = random.nextInt(201) - 100;
                    int newScore = Math.max(0, user.getScore() + variation);
                    user.setScore(newScore);
                });
                UsersLiveStateHandler.getHandler().broadcastUpdate();
            }
        }, 100, 1000);
    }

    public User getUserByName(String userName) {
        return usersMap.get(userName);
    }

    private void register(User... users) {
        for (User user : users) {
            usersMap.put(user.getName(), user);
        }
    }

    public List<UserDTO> getUsers() {
        return usersMap.values().stream()
                .sorted(Comparator.comparingInt(User::getScore).reversed())
                .map(UserDTO::new)
                .toList();
    }

    public static UserService get() {
        return service;
    }
}
