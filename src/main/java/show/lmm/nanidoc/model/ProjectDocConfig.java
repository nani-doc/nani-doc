package show.lmm.nanidoc.model;

import lombok.Data;

import java.util.Map;

/**
 * 项目文档配置
 */
@Data
public class ProjectDocConfig {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目标题
     */
    private String projectTitle;

    /**
     * 模块列表，例：{"user":"用户"}
     */
    private Map<String, String> moduleMap;

    /**
     * 默认模块名称，模块数量大于1时需要配置
     */
    private String defaultModuleName;
}
