package com.gulfnet.tmt.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    public static String generatePatternedPassword(int length) {
        StringBuilder password = new StringBuilder();

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            // Use modulo to switch between uppercase, lowercase, and numbers
            if (i % 3 == 0) {
                password.append(generateRandomUppercaseLetter(random));
            } else if (i % 3 == 1) {
                password.append(generateRandomLowercaseLetter(random));
            } else {
                password.append(generateRandomDigit(random));
                password.append(generateRandomSpecailCharacter(random));
            }
        }

        return password.toString();
    }

    private static char generateRandomUppercaseLetter(SecureRandom random) {
        return (char) ('A' + random.nextInt(26));
    }

    private static char generateRandomLowercaseLetter(SecureRandom random) {
        return (char) ('a' + random.nextInt(26));
    }

    private static char generateRandomDigit(SecureRandom random) {
        return (char) ('0' + random.nextInt(10));
    }
    private static char generateRandomSpecailCharacter(SecureRandom random) {
        char[] specailChar = {'@','#','$','!',':','*','&'};
        return specailChar[random.nextInt(0, specailChar.length-1)];
    }

}
