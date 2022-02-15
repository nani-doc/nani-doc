package show.lmm.nanidoc.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IO工具类
 *
 * @author 刘明明
 * @date 2021-11-15 11:23
 */
public class IoUtil {

    /**
     * 查询文件列表
     *
     * @param dirOrFile: 文件夹或文件
     * @param suffix:  文件后缀
     * @return java.util.List<java.io.File>
     * @modify 刘明明/2021-11-15 11:24:28
     **/
    public static List<File> listFileOnly(File dirOrFile, String... suffix) {
        if (!dirOrFile.exists()) {
            throw new IllegalArgumentException("listFileOnly [" + dirOrFile.getAbsolutePath() + "] non exist.");
        }
        return listFile(dirOrFile, 1).stream()
                .filter(file -> {
                    if (suffix == null) {
                        return true;
                    }
                    String fileName = file.getName();
                    return Arrays.stream(suffix).anyMatch(fileName::endsWith);
                }).collect(Collectors.toList());
    }

    /**
     * 查询文件列表
     *
     * @param dirOrFile: 文件夹或文件
     * @param mode:  模式：罗列模式(0-罗列文件和文件夹； 1-只罗列文件； 2-只罗列文件夹)
     * @return java.util.List<java.io.File>
     * @modify 刘明明/2021-11-15 11:25:29
     **/
    public static List<File> listFile(File dirOrFile, int mode) {
        List<File> fileContainer = new ArrayList<>(16);
        listFile(dirOrFile, fileContainer, mode);
        return fileContainer;
    }

    /**
     * 查询文件列表
     *
     * @param dirOrFile: 文件夹或文件
     * @param fileContainer: 罗列结果
     * @param mode:  模式：罗列模式(0-罗列文件和文件夹； 1-只罗列文件； 2-只罗列文件夹)
     * @modify 刘明明/2021-11-15 11:33:42
     **/
    public static void listFile(File dirOrFile, List<File> fileContainer, int mode) {
        if (!dirOrFile.exists()) {
            return;
        }
        int onlyDirMode = 2;
        if (mode != 0 && mode != 1 && mode != onlyDirMode) {
            throw new IllegalArgumentException("mode [" + mode + "] is non-supported. 0,1,2is only support.");
        }
        if (dirOrFile.isDirectory()) {
            File[] files = dirOrFile.listFiles();
            if (files != null) {
                for (File f : files) {
                    listFile(f, fileContainer, mode);
                }
            }
            if (mode == 0 || mode == onlyDirMode) {
                fileContainer.add(dirOrFile);
            }
        } else {
            if (mode == 0 || mode == 1) {
                fileContainer.add(dirOrFile);
            }
        }
    }

    /**
     * inputStream转换为byte
     *
     * @param inputStream:  输入流
     * @param needCloseIo:  是否需要关闭io
     * @return byte[]
     * @modify 刘明明/2021-11-15 11:34:22
     **/
    public static byte[] toBytes(InputStream inputStream,boolean needCloseIo) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try {
                byte[] buffer = new byte[4096];
                int n;
                while (-1 != (n = inputStream.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                return output.toByteArray();
            } finally {
                if(needCloseIo) {
                    close(output, inputStream);
                }
            }
        }
    }

    /**
     * 关闭流
     *
     * @param ioArr:  待关闭的io
     * @modify 刘明明/2021-11-15 11:34:57
     **/
    public static void close(Closeable... ioArr) {
        if (ioArr == null) {
            return;
        }
        for (Closeable io : ioArr) {
            if (io == null) {
                continue;
            }
            try {
                io.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
