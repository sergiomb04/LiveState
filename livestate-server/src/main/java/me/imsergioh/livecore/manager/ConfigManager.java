package me.imsergioh.livecore.manager;

import lombok.Getter;
import me.imsergioh.livecore.config.MainConfig;

public class ConfigManager {

    @Getter
    private static final MainConfig config = new MainConfig();

}
