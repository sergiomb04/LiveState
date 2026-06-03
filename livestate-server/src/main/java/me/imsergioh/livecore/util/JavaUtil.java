package me.imsergioh.livecore.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaUtil {

    public static Path getApplicationDirectory() {
        return Paths.get(System.getProperty("user.dir"));
    }

}
