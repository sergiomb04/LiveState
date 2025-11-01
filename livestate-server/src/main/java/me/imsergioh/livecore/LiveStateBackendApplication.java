package me.imsergioh.livecore;

import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.ILiveStateHandler;
import me.imsergioh.livecore.manager.ClientActionsManager;
import me.imsergioh.livecore.service.UserService;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class LiveStateBackendApplication {

    private static final Map<String, ILiveStateHandler<?>> channelsHandler = new HashMap<>();

    public static void main(String[] args) {
        JwtUtil.init();
        ClientActionsManager.init();

        registerHandler(
                new UserLiveStateHandler(),
                new UsersLiveStateHandler())
        ;

        SpringApplication.run(LiveStateBackendApplication.class, args);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                User user = UserService.get().getUserByName("Sergio");
                user.setScore(user.getScore() + 1);
            }
        }, 1000, 1000);
    }

    public static Object getData(String channelName, Map<String, String> params) {
        ILiveStateHandler<?> handler = channelsHandler.get(channelName);
        return handler.getData(params);
    }

    private static void registerHandler(ILiveStateHandler<?>... handlers) {
        for (ILiveStateHandler<?> handler : handlers) {
            channelsHandler.put(handler.getWebSocketChannelName(), handler);
        }
    }

    private static void generateTestTokens() {
        String bobToken = JwtUtil.generateToken("Bob", Map.of("role", "USER"));
        System.out.println("BOB: " + bobToken);

        String sergioToken = JwtUtil.generateToken("Sergio", Map.of("role", "ADMIN"));
        System.out.println("Sergio: " + sergioToken);
    }
}