package me.imsergioh.livecore.manager;

import lombok.Getter;
import me.imsergioh.livecore.LiveStateBackendApplication;
import me.imsergioh.livecore.instance.config.JsonConfig;
import me.imsergioh.livecore.util.JavaUtil;

public class ConfigManager {

    @Getter
    private static final JsonConfig config;

    static {
        config = new JsonConfig(JavaUtil.getJarPath(LiveStateBackendApplication.class) + "/config.json");
    }

}
