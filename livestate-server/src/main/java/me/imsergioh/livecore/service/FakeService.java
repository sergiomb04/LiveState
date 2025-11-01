package me.imsergioh.livecore.service;

import lombok.Getter;
import me.imsergioh.livecore.handler.FakePlayerHandler;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class FakeService {

    @Getter
    private static final FakeService service = new FakeService();

    @Getter
    private final FakePlayer fakePlayer = new FakePlayer("ImSergioh");

    public static void init() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                service.fakePlayer.update();
            }
        }, 1000, 1000);
    }

    public class FakePlayer {
        String name;
        UUID uuid;

        private FakePlayer(String name) {
            this.name = name;
            update();
        }

        private void update() {
            uuid = UUID.randomUUID();
            FakePlayerHandler.getHandler().broadcastUpdate();
        }
    }

}
