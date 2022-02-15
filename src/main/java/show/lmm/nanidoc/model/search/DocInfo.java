package show.lmm.nanidoc.model.search;

import lombok.Data;

/**
 * 搜索索引文档信息
 *
 * @author 刘明明
 * @date 2022-01-30 11:57
 */
@Data
public class DocInfo {

    /**
     * url路径
     */
    private String path;

    /**
     * 模块标题
     */
    private String moduleTitle;

    /**
     * 控制器标题
     */
    private String controllerTitle;

    /**
     * 接口标题
     */
    private String actionTitle;

    /**
     * 接口url
     */
    private  String actionUrl;
}
