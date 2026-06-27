package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.instance.handler.WSChannelName;
import me.imsergioh.livecore.service.FakeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@WSChannelName("fakePlayer")
public class FakePlayerHandler implements LiveStateHandler<FakeService.FakePlayer> {

    @Getter
    private static final FakePlayerHandler handler = new FakePlayerHandler();

    @Override
    @GetMapping("/api/fakePlayer")
    public FakeService.FakePlayer getData(@PathVariable Map<String, String> params) {
        return FakeService.getService().getFakePlayer();
    }

    @Override
    public void onMessage(Map<String, Object> payload) {
        String name = (String) payload.get("name");
        UUID uuid = UUID.fromString((String) payload.get("uuid"));
        FakeService.getService().setFakePlayer(name, uuid);
    }
}
