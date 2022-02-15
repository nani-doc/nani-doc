package show.lmm.nanidoc.model;

import lombok.Data;

import java.util.List;

/**
 * 项目文档信息
 */
@Data
public class ProjectDocInfo {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目标题
     */
    private String projectTitle;

    /**
     * 模块列表
     */
    private List<ModuleInfo> modules;
}
