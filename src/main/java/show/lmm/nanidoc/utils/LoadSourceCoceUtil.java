package show.lmm.nanidoc.utils;

import com.thoughtworks.qdox.library.ClassLibraryBuilder;
import com.thoughtworks.qdox.library.SortedClassLibraryBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 加载jar工具类
 *
 * @author 刘明明
 * @date 2021-11-12 17:21
 */
public class LoadSourceCoceUtil {

    private static final String JAR_SUFFIX = ".jar";

    private static final String SOURCE_CODE_SUFFIX = ".java";

    private static final int SOURCE_CODE_SUFFIX_LENGTH = SOURCE_CODE_SUFFIX.length();

    /**
     * 加载指定的jar文件中的所有class（或： 加载指定目录(含其子孙目录)下的所有jar文件中的所有class）
     * <p>
     * 注:普通的jar包与spring-boot jar包都支持。
     *
     * @param jarOrDirFile     要加载的jar文件(或jar文件所在的目录)
     *                         <br/>
     *                         注：如果jarOrDir是目录，那么该目录包括其子孙目录下的所有jar都会被加载。
     * @param includePrefixSet 当通过前缀控制是否实例化Class对象
     *                         <br />
     *                         注: 若includePrefixSet为null或者为空集合，那么默认实例化所有的class
     * @param excludePrefixSet 通过前缀控制是否排除实例化Class对象
     *                         <br />
     *                         注: excludePrefixSet优先级高于includePrefixSet。
     * @return 已加载了的class实例集合
     */
    public static void loadSourceCode(String[] jarOrDirFile,
                                      Set<String> includePrefixSet,
                                      Set<String> excludePrefixSet,
                                      ClassLibraryBuilder libraryBuilder) {
        for (String filePath : jarOrDirFile) {
            loadSourceCode(filePath, includePrefixSet, excludePrefixSet, libraryBuilder);
        }
    }

    /**
     * 加载指定的jar文件中的所有class（或： 加载指定目录(含其子孙目录)下的所有jar文件中的所有class）
     * <p>
     * 注:普通的jar包与spring-boot jar包都支持。
     *
     * @param jarOrDirFile     要加载的jar文件(或jar文件所在的目录)
     *                         <br/>
     *                         注：如果jarOrDir是目录，那么该目录包括其子孙目录下的所有jar都会被加载。
     * @param includePrefixSet 当通过前缀控制是否实例化Class对象
     *                         <br />
     *                         注: 若includePrefixSet为null或者为空集合，那么默认实例化所有的class
     * @param excludePrefixSet 通过前缀控制是否排除实例化Class对象
     *                         <br />
     *                         注: excludePrefixSet优先级高于includePrefixSet。
     * @return 已加载了的class实例集合
     */
    public static void loadSourceCode(String jarOrDirFile,
                                      Set<String> includePrefixSet,
                                      Set<String> excludePrefixSet,
                                      ClassLibraryBuilder libraryBuilder) {
        if(jarOrDirFile==null||jarOrDirFile.isEmpty()){
            return;
        }
        loadSourceCode(new File(jarOrDirFile), includePrefixSet, excludePrefixSet, libraryBuilder);
    }

    /**
     * 加载指定的jar文件中的所有class（或： 加载指定目录(含其子孙目录)下的所有jar文件中的所有class）
     * <p>
     * 注:普通的jar包与spring-boot jar包都支持。
     *
     * @param jarOrDirFile     要加载的jar文件(或jar文件所在的目录)
     *                         <br/>
     *                         注：如果jarOrDir是目录，那么该目录包括其子孙目录下的所有jar都会被加载。
     * @param includePrefixSet 当通过前缀控制是否实例化Class对象
     *                         <br />
     *                         注: 若includePrefixSet为null或者为空集合，那么默认实例化所有的class
     * @param excludePrefixSet 通过前缀控制是否排除实例化Class对象
     *                         <br />
     *                         注: excludePrefixSet优先级高于includePrefixSet。
     * @return 已加载了的class实例集合
     */
    public static void loadSourceCode(File jarOrDirFile,
                                      Set<String> includePrefixSet,
                                      Set<String> excludePrefixSet,
                                      ClassLibraryBuilder libraryBuilder) {
        if (jarOrDirFile == null || !jarOrDirFile.exists()) {
            throw new RuntimeException(String.format("jarOrDirFile is null Or jarOrDirFile is non-exist. %s",jarOrDirFile.getPath()));
        }

        List<File> jarFileList = IoUtil.listFileOnly(jarOrDirFile, JAR_SUFFIX);
        List<File> normalJarFileList = new ArrayList<>(16);
        jarFileList.forEach(jar -> {
            normalJarFileList.add(jar);
        });
        loadSourceCode(normalJarFileList, true, includePrefixSet, excludePrefixSet, libraryBuilder);
    }

    /**
     * 加载(普通)jar文件(中的所有class)
     * <p>
     * 注: jar文件中若包含其他的的jar文件，其他的jar文件里面的class是不会被加载的。
     *
     * @param jarFileList      要加载的jar文件集合
     * @param instanceClass    是否实例化Class对象
     * @param includePrefixSet 当instanceClass为true时， 通过前缀控制是否实例化Class对象
     *                         <br />
     *                         注: 若includePrefixSet为null或者为空集合，那么默认实例化所有的class
     * @param excludePrefixSet 当instanceClass为true时， 通过前缀控制是否排除实例化Class对象
     *                         <br />
     *                         注: excludePrefixSet优先级高于includePrefixSet。
     */
    private static void loadSourceCode(List<File> jarFileList,
                                       boolean instanceClass,
                                       Set<String> includePrefixSet,
                                       Set<String> excludePrefixSet,
                                       ClassLibraryBuilder libraryBuilder) {
        if (jarFileList == null || jarFileList.size() == 0) {
            return;
        }
        verifyJarFile(jarFileList);
        try {
            for (File jar : jarFileList) {
                if (!instanceClass) {
                    continue;
                }
                ZipInputStream zipFile = null;
                zipFile = new ZipInputStream(new FileInputStream(jar));
                ZipEntry zipEntry;
                //遍历压缩文件内部 文件数量
                while ((zipEntry = zipFile.getNextEntry()) != null) {
                    String zipEntryName = zipEntry.getName();
                    if (!zipEntryName.endsWith(SOURCE_CODE_SUFFIX)) {
                        continue;
                    }
                    String classLongName = zipEntryName
                            .substring(0, zipEntryName.length() - SOURCE_CODE_SUFFIX_LENGTH)
                            .replace("/", ".");
                    if (excludePrefixSet != null && excludePrefixSet.size() > 0) {
                        if (excludePrefixSet.stream().anyMatch(classLongName::startsWith)) {
                            continue;
                        }
                    }
                    if (includePrefixSet != null && includePrefixSet.size() > 0) {
                        if (includePrefixSet.stream().noneMatch(classLongName::startsWith)) {
                            continue;
                        }
                    }
                    byte[] sourceCode = IoUtil.toBytes(zipFile, false);
                    libraryBuilder.addSource(new ByteArrayInputStream(sourceCode));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验jar文件合法性(存在 && 是.jar后缀的文件)
     *
     * @param jarFileList 要校验的jar文件
     */
    private static void verifyJarFile(List<File> jarFileList) {
        Objects.requireNonNull(jarFileList, "jarFileList cannot be empty.");
        jarFileList.forEach(file -> {
            if (!file.exists()) {
                throw new IllegalArgumentException("file [" + file.getAbsolutePath() + "] non-exist.");
            }
            if (!file.getName().endsWith(JAR_SUFFIX)) {
                throw new IllegalArgumentException("file [" + file.getAbsolutePath() + "] is not a jar file.");
            }
        });
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        Set<String> includePrefixSet = new HashSet<>();
        Set<String> excludePrefixSet = new HashSet<>();
        excludePrefixSet.add("BOOT-INF.classes");
        ClassLibraryBuilder libraryBuilder = new SortedClassLibraryBuilder();
        // 测试加载jar
        loadSourceCode(new File("D:\\project\\rd-team\\springcloud-commonbase\\build\\libs\\commonBase-2.0.3-sources.jar"),
                includePrefixSet,
                excludePrefixSet, libraryBuilder
        );
    }
}
