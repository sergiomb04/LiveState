package me.imsergioh.livecore.service;

import lombok.Getter;
import me.imsergioh.livecore.handler.FakePlayerHandler;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FakeService {

    @Getter
    private static final FakeService service = new FakeService();

    @Getter
    private final FakePlayer fakePlayer = new FakePlayer("ImSergioh");

    @Getter
    public class FakePlayer {
        String name;
        UUID uuid;

        private FakePlayer(String name) {
            this.name = name;
            uuid = UUID.randomUUID();
        }

        private void update() {
            uuid = UUID.randomUUID();
            FakePlayerHandler.getHandler().broadcastUpdate();
        }
    }

}
