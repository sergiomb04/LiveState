package me.imsergioh.livecore;

import me.imsergioh.livecore.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveStateBackendApplication {

    public static void main(String[] args) {
        JwtUtil.init();
        SpringApplication.run(LiveStateBackendApplication.class, args);
    }
}