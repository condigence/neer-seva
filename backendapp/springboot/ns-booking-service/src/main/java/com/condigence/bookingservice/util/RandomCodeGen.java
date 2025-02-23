package com.condigence.bookingservice.util;

import java.nio.charset.Charset;
import java.util.Random;

public class RandomCodeGen {
    public static void main(String[] args) {
        String generatedString = genRandom();
        System.out.println(generatedString);
    }

    public static String genRandom() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
