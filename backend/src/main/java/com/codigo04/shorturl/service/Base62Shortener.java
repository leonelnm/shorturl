package com.codigo04.shorturl.service;

import java.security.SecureRandom;

public final class Base62Shortener {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomBase62(int length) {
        long max = (long) Math.pow(62, length) - 1;
        long randomNum = Math.abs(RANDOM.nextLong()) % max;
        return encodeBase62(randomNum);
    }

    private static String encodeBase62(long num) {
        StringBuilder sb = new StringBuilder();
        if (num == 0) return "0";
        while (num > 0) {
            int rem = (int)(num % 62);
            sb.append(BASE62.charAt(rem));
            num /= 62;
        }
        return sb.reverse().toString();
    }

}
