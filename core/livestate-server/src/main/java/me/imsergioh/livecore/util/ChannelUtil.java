package me.imsergioh.livecore.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChannelUtil {

    public static Map<String, String> extractParams(String pattern, String channel) {
        if (pattern.equals(channel)) {
            return Map.of(); // Son iguales, no hay parámetros
        }

        String[] patternParts = pattern.split("/");
        String[] channelParts = channel.split("/");

        if (patternParts.length != channelParts.length) {
            return Map.of(); // No coinciden en longitud, no se puede extraer
        }

        Map<String, String> params = new LinkedHashMap<>(); // Mantiene el orden
        for (int i = 0; i < patternParts.length; i++) {
            String p = patternParts[i];
            String c = channelParts[i];

            if (p.startsWith("{") && p.endsWith("}")) {
                String key = p.substring(1, p.length() - 1);
                params.put(key, c);
            } else if (!p.equals(c)) {
                // Patrón literal diferente al canal, no hay match
                return Map.of();
            }
        }

        return params;
    }

}
