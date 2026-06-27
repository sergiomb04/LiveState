package me.imsergioh.livecore.action;

import me.imsergioh.livecore.instance.connection.IConnectionAction;
import me.imsergioh.livecore.instance.connection.LiveStateClient;
import me.imsergioh.livecore.service.FakePlayersService;

import java.util.Map;

public class AddFakePlayerAction implements IConnectionAction {

    @Override
    public String getName() {
        return "addFakePlayer";
    }

    @Override
    public void onAction(LiveStateClient client, Map<String, Object> payload) {
        FakePlayersService.getService().addPlayer((String) payload.get("name"));
    }
}
