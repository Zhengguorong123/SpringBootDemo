package gwan.zheng.core.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RandomCodeGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * 生成多个随机码
     * @param length 每个随机码长度
     * @param count  需要生成多少个
     * @return 随机码列表
     */
    public static List<String> generateCodes(int length, int count) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            codes.add(generateOneCode(length));
        }
        return codes;
    }

    /**
     * 生成单个随机码
     */
    public static String generateOneCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(index));
        }
        return sb.toString();
    }
}
