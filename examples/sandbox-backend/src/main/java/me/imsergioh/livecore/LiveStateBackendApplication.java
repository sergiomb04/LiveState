package me.imsergioh.livecore;

import me.imsergioh.livecore.handler.ChannelsHandler;
import me.imsergioh.livecore.handler.FakePlayerHandler;
import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.manager.ClientActionsManager;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Map;

@SpringBootApplication
public class LiveStateBackendApplication {

    public static void main(String[] args) {
        try {
            JwtUtil.init(me.imsergioh.livecore.config.MainConfig.getSecret(), me.imsergioh.livecore.config.MainConfig.getExpirationSecs(), me.imsergioh.livecore.config.MainConfig.getIssuer());
            generateTestTokens();
            ClientActionsManager.init();

            ChannelsHandler.registerHandler(
                    new UserLiveStateHandler(),
                    new UsersLiveStateHandler(),
                    new FakePlayerHandler()
            );

            SpringApplication.run(LiveStateBackendApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateTestTokens() {
        String bobToken = JwtUtil.generateToken("Bob", Map.of("role", "USER"));
        System.out.println("BOB: " + bobToken);

        String sergioToken = JwtUtil.generateToken("Sergio", Map.of("role", "ADMIN"));
        System.out.println("Sergio: " + sergioToken);
    }
}