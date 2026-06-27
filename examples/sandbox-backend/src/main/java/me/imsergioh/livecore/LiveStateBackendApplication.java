package me.imsergioh.livecore;

import me.imsergioh.livecore.action.AddFakePlayerAction;
import me.imsergioh.livecore.config.MainConfig;
import me.imsergioh.livecore.handler.ChannelsHandler;
import me.imsergioh.livecore.handler.FakePlayersHandler;
import me.imsergioh.livecore.handler.UserLiveStateHandler;
import me.imsergioh.livecore.handler.UsersLiveStateHandler;
import me.imsergioh.livecore.manager.ClientActionsManager;
import me.imsergioh.livecore.service.FakePlayersService;
import me.imsergioh.livecore.service.UserService;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class LiveStateBackendApplication {

    public static void main(String[] args) {
        try {
            JwtUtil.init(MainConfig.getSecret(), MainConfig.getExpirationSecs(), MainConfig.getIssuer());
            generateTestTokens();
            ClientActionsManager.init();

            ClientActionsManager.register(new AddFakePlayerAction());

            ClientActionsManager.register("removeFakePlayer",
                    (payload) -> FakePlayersService.getService().removePlayer(UUID.fromString((String) payload.get("uuid"))));

            ChannelsHandler.registerHandler(
                    new UserLiveStateHandler(),
                    new UsersLiveStateHandler(),
                    new FakePlayersHandler()
            );

            ChannelsHandler.registerChannelInterceptor("testChannel", (payload) -> System.out.println("TEST CHANNEL RECEIVED! " + payload));

            UserService.get().startSimulatingRealtimeScores();

            FakePlayersService.getService().addPlayer("Pepito");
            FakePlayersService.getService().addPlayer("Sergio");
            FakePlayersService.getService().addPlayer("Miguel");

            SpringApplication.run(LiveStateBackendApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private static void generateTestTokens() {
        String bobToken = JwtUtil.generateToken("Bob", Map.of("role", "USER"));
        System.out.println("BOB: " + bobToken);

        String sergioToken = JwtUtil.generateToken("Sergio", Map.of("role", "ADMIN"));
        System.out.println("Sergio: " + sergioToken);
    }
}