package show.lmm.nanidoc.core;

import show.lmm.nanidoc.model.ArgInfo;

import java.util.*;

/**
 * 常量
 */
public class Constant {

    /**
     * 请求mapping列表
     */
    public static final Collection<String> requestMappingAnnotationList = new HashSet<String>() {{
        add("org.springframework.web.bind.annotation.RequestMapping");
        add("org.springframework.web.bind.annotation.GetMapping");
        add("org.springframework.web.bind.annotation.PostMapping");
        add("org.springframework.web.bind.annotation.DeleteMapping");
        add("org.springframework.web.bind.annotation.PatchMapping");
        add("org.springframework.web.bind.annotation.PutMapping");
    }};

    /**
     * 输入参数
     */
    public static final List<ArgInfo> args = new ArrayList<ArgInfo>() {{
        add(new ArgInfo() {{
            setArgName("package");
            setTitle("controller包名");
            setExample("show.lmm.controller");
            setRequired(true);
        }});
        add(new ArgInfo() {{
            setArgName("sourceJars");
            setTitle("第三方源码jar包列表");
            setExample("\"c:/a.jar,d:/b.jar\"");
            setRequired(false);
        }});
        add(new ArgInfo() {{
            setArgName("sourcePath");
            setTitle("源码目录");
            setRequired(false);
            setDescription("默认为当前目录");
        }});
        add(new ArgInfo() {{
            setArgName("docOutPath");
            setTitle("文档输出目录");
            setRequired(false);
            setDescription("默认为：./distDoc 目录");
        }});
        add(new ArgInfo() {{
            setArgName("wraperClasses");
            setTitle("包裹class列表");
            setRequired(false);
            setDescription("默认为空");
            setExample("\"reactor.core.publisher.Mono,reactor.core.publisher.flux\"");
        }});
    }};

    /**
     * 帮助参数key
     */
    public static final String helpArgKey = "--help";

    /**
     * 输入参数key列表
     */
    public static final Map<String, Integer> argKeys = new HashMap<>();

    /**
     * 最大输入参数长度
     */
    public static int maxArgKeyLength = 0;

    static {
        int index = 0;
        int argNameLength;
        for (ArgInfo item : args) {
            argNameLength = item.getArgName().length();
            if (argNameLength > maxArgKeyLength) {
                maxArgKeyLength = argNameLength;
            }
            argKeys.put(item.getArgName(), index++);
        }
    }
}
