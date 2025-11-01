package me.imsergioh.livecore;

import me.imsergioh.livecore.manager.ClientActionsManager;
import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class LiveStateBackendApplication {

    public static void main(String[] args) {
        JwtUtil.init();
        ClientActionsManager.init();
        SpringApplication.run(LiveStateBackendApplication.class, args);
    }

    private static void generateTestTokens() {
        String bobToken = JwtUtil.generateToken("Bob", Map.of("role", "USER"));
        System.out.println("BOB: " + bobToken);

        String sergioToken = JwtUtil.generateToken("Sergio", Map.of("role", "ADMIN"));
        System.out.println("Sergio: " + sergioToken);
    }
}