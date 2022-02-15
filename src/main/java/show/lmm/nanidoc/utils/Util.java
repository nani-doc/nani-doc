package show.lmm.nanidoc.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 通用工具类
 */
public class Util {

    /**
     * 首字母转小写
     *
     * @param str 字符串
     */
    public static String firstCharToLowerCase(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() < 2) {
            return str.toLowerCase(Locale.ROOT);
        }
        return String.format("%s%s", String.valueOf(str.charAt(0)).toLowerCase(Locale.ROOT), str.substring(1));
    }

    /**
     * InputStream转换成byte数组
     *
     * @param stream InputStream
     * @return byte[]
     */
    public static byte[] streamToByteList(InputStream stream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int index;
        try {
            while ((index = stream.read(buff, 0, 100)) > 0) {
                outputStream.write(buff, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
        return outputStream.toByteArray();
    }
}
