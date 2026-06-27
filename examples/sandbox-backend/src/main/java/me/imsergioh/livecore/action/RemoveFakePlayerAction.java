package me.imsergioh.livecore.action;

import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.service.FakePlayersService;

import java.util.Map;
import java.util.UUID;

public class RemoveFakePlayerAction implements IConnectionAction {
    @Override
    public String getName() {
        return "removeFakePlayer";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> payload) {
        FakePlayersService.getService().removePlayer(UUID.fromString((String) payload.get("uuid")));
    }
}
