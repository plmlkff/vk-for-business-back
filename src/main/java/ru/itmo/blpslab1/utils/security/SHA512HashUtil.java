package ru.itmo.blpslab1.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class SHA512HashUtil {
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 алгоритм не поддерживается");
        }
    }

    public static boolean check(String password, String hash) {
        return hash(password).equals(hash);
    }
}
