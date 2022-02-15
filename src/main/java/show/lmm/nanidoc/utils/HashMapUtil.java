package show.lmm.nanidoc.utils;

import java.util.HashMap;

/**
 * HashMap工具类
 *
 * @author 刘明明
 * @date 2021-11-15 12:12
 */
public class HashMapUtil {

    /**
     * 阈值
     */
    private static final int THRESHOLD = 3;

    /**
     * 初始化HashMap
     *
     * @param expectedSize: 初始容量
     * @return java.util.HashMap<K, V>
     * @modify 刘明明/2021-11-15 12:16:02
     **/
    public static <K, V> HashMap<K, V> init(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }

    /**
     * 计算HashMap初始化容量
     *
     * @param expectedSize: 初始容量
     * @return int
     * @modify 刘明明/2021-11-15 12:16:22
     **/
    private static int capacity(int expectedSize) {
        if (expectedSize < THRESHOLD) {
            return expectedSize + 1;
        } else {
            return expectedSize < 1073741824 ? (int) (expectedSize / 0.75F + 1.0F) : 2147483647;
        }
    }
}
