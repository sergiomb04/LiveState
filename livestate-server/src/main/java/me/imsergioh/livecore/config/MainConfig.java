package me.imsergioh.livecore.config;

import me.imsergioh.livecore.LiveStateBackendApplication;
import me.imsergioh.livecore.manager.ConfigManager;
import me.imsergioh.livecore.util.JavaUtil;

public class MainConfig extends me.imsergioh.livecore.instance.config.JsonConfig {

    public MainConfig() {
        super(JavaUtil.getJarPath(LiveStateBackendApplication.class) + "/config.json");
        register("JWT_SECRET", "EXAMPLE_8f7b0c9a02e9481d9a3e4a5c93b14c0f");
        register("JWT_ISSUER", "mi-example-backend-api");
        register("JWT_EXPIRATION_SECS", 86_400);
        register("REFRESH_TOKEN_SECRET", "EXAMPLE_e9d812f0b7c24197b5dce3b63d13c932");
        register("REFRESH_TOKEN_EXPIRATION_SECS", 604_800);
        register("TOKEN_PREFIX", "Bearer");
        register("AUTH_HEADER", "Authorization");
        register("SEND_INIT_DATA_ON_CONNECT_WS", false);
        save();
    }

    public static boolean sendInitDataOnConnectWebSocket() {
        return ConfigManager.getConfig().getBoolean("SEND_INIT_DATA_ON_CONNECT_WS", false);
    }

    public static String getTokenPrefix() {
        return ConfigManager.getConfig().getString("TOKEN_PREFIX", "Bearer");
    }

    public static String getAuthHeader() {
        return ConfigManager.getConfig().getString("AUTH_HEADER", "Authorization");
    }

    public static long getExpirationSecs() {
        return ConfigManager.getConfig().getLong("JWT_EXPIRATION_SECS", -1L);
    }

    public static String getIssuer() {
        return ConfigManager.getConfig().getString("JWT_ISSUER", null);
    }

    public static String getSecret() {
        return ConfigManager.getConfig().getString("JWT_SECRET", null);
    }

}
