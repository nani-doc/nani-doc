package show.lmm.nanidoc.utils;

import java.io.File;
import java.net.URL;
import java.util.jar.Manifest;

/**
 * MANIFEST工具类
 *
 * @author 刘明明
 * @date 2022-02-07 13:36
 */
public class ManifestUtil {

    /**
     * 获得值
     *
     * @param key:  键
     * @return java.lang.String
     * @since 刘明明/2022-02-07 14:28:03
     **/
    public static String getValue(String key) {
        Class<?> clazz = ManifestUtil.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        String manifestPath;
        if (!classPath.startsWith("jar")) {
            String relativePath = clazz.getName().replace('.', File.separatorChar) + ".class";
            String classFolder = classPath.substring(0, classPath.length() - relativePath.length() - 1);
            manifestPath = classFolder;
        } else {
            manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1);
        }
        return getValue(manifestPath + "/META-INF/MANIFEST.MF", key);
    }

    /**
     * 获得值
     *
     * @param manifestPath: MANIFEST路径
     * @param key:  键
     * @return java.lang.String
     * @since 刘明明/2022-02-07 14:29:30
     **/
    private static String getValue(String manifestPath, String key) {
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            return manifest.getMainAttributes().getValue(key);
        } catch (Exception e) {
        }
        return "";
    }
}
