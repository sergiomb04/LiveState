package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.instance.handler.WSChannelName;
import me.imsergioh.livecore.service.FakePlayersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@WSChannelName("fakePlayers")
public class FakePlayersHandler implements LiveStateHandler<List<FakePlayersService.FakePlayer>> {

    @Getter
    private static final FakePlayersHandler handler = new FakePlayersHandler();

    @Override
    @GetMapping("/api/fakePlayers")
    public List<FakePlayersService.FakePlayer> getData(@PathVariable Map<String, String> params) {
        return FakePlayersService.getService().getPlayers();
    }
}
