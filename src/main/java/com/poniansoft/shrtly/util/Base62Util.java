package com.poniansoft.shrtly.util;

public class Base62Util {
    private static final String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    // Encodes a long value to a Base62 string
    public static String encode(long value) {
        if (value == 0) {
            return "0";
        }

        value = value < 0 ? Integer.toUnsignedLong((int) value) : value;

        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62_ALPHABET.charAt((int) (value % BASE)));
            value /= BASE;
        }
        return sb.reverse().toString();
    }

    // Decodes a Base62 string back to a long value
    public static long decode(String base62String) {
        long result = 0;
        for (char c : base62String.toCharArray()) {
            result = result * BASE + BASE62_ALPHABET.indexOf(c);
        }
        return result;
    }
}
