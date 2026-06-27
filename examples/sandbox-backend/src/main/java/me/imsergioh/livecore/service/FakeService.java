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
    private FakePlayer fakePlayer = new FakePlayer("ImSergioh");

    public void setFakePlayer(String name, UUID uuid) {
        fakePlayer = new FakePlayer(name, uuid);
    }

    @Getter
    public static class FakePlayer {
        String name;
        UUID uuid;

        private FakePlayer(String name) {
            this.name = name;
            uuid = UUID.randomUUID();
        }

        private FakePlayer(String name, UUID uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        private void update() {
            uuid = UUID.randomUUID();
            FakePlayerHandler.getHandler().broadcastUpdate();
        }
    }

}
