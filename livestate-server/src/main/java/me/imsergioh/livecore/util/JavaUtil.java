package me.imsergioh.livecore.util;

import lombok.SneakyThrows;

import java.io.File;

public class JavaUtil {

    @SneakyThrows
    public static String getJarPath(Class<?> clazz) {
        return new File(clazz.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()).getAbsolutePath();
    }

}
