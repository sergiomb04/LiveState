package me.imsergioh.livecore.service;

import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.instance.User;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<User> getUsers() {
        return usersMap.values().stream()
                .sorted(Comparator.comparingInt(User::getScore).reversed())
                .toList();
    }

    public static UserService get() {
        return service;
    }
}
