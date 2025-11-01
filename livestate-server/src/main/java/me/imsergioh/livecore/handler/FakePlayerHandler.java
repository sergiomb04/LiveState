package me.imsergioh.livecore.handler;

import lombok.Getter;
import me.imsergioh.livecore.instance.User;
import me.imsergioh.livecore.instance.handler.LiveStateHandler;
import me.imsergioh.livecore.instance.handler.WSChannelName;
import me.imsergioh.livecore.service.FakeService;
import me.imsergioh.livecore.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
}
