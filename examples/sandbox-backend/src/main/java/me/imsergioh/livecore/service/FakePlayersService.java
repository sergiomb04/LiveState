package me.imsergioh.livecore.service;

import lombok.Getter;
import me.imsergioh.livecore.handler.FakePlayersHandler;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Service
public class FakePlayersService {

    @Getter
    private static final FakePlayersService service = new FakePlayersService();

    private final List<FakePlayer> players = new ArrayList<>();

    public void addPlayer(String name) {
        players.add(new FakePlayer(name));
    }

    public void removePlayer(String name) {
        Optional<FakePlayer> optional = players.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (optional.isEmpty()) return;
        FakePlayer player = optional.get();
        players.remove(player);
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
            FakePlayersHandler.getHandler().broadcastUpdate();
        }
    }

}
