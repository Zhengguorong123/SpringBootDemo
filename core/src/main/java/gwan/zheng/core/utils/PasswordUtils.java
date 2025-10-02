package gwan.zheng.core.utils;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-17:11
 * @Description:
 */

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    private static final int SALT_LENGTH = 16; // 16 bytes
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256; // 256 bits

    // 生成随机盐
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 根据密码和盐生成哈希
    public static String hashPassword(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    Base64.getDecoder().decode(salt),
                    ITERATIONS,
                    KEY_LENGTH
            );
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    // 验证密码
    public static boolean verifyPassword(String password, String salt, String expectedHash) {
        String hash = hashPassword(password, salt);
        return hash.equals(expectedHash);
    }
}

