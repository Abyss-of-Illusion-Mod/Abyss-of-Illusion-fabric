package com.aoimod.utils;

import java.math.BigInteger;

public class Base37 {

    private static final String BASE37_ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789_";
    private static final int BASE = BASE37_ALPHABET.length();

    public static String encode(String input) {
        BigInteger number = new BigInteger(1, input.getBytes());
        StringBuilder encoded = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = number.divideAndRemainder(BigInteger.valueOf(BASE));
            int remainder = divRem[1].intValue();
            encoded.append(BASE37_ALPHABET.charAt(remainder));
            number = divRem[0];
        }

        return encoded.reverse().toString();
    }

    public static String decode(String encoded) {
        BigInteger number = BigInteger.ZERO;

        for (char c : encoded.toCharArray()) {
            int index = BASE37_ALPHABET.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid character in encoded string: " + c);
            }
            number = number.multiply(BigInteger.valueOf(BASE)).add(BigInteger.valueOf(index));
        }

        byte[] bytes = number.toByteArray();

        // 處理可能的前導零
        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }

        return new String(bytes);
    }
}
