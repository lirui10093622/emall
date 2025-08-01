package org.emall.facade.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomTextUtils {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}