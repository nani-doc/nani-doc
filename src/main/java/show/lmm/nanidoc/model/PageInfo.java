package show.lmm.nanidoc.model;

import lombok.Data;

/**
 * 页面信息
 * */
@Data
public class PageInfo {

    /**
     * 页面名称
     * */
    private String name;

    /**
     * 页面路径
     * */
    private String path;

    /**
     * 控制器标题
     */
    private String controllerTitle;

    /**
     * 控制器名称
     * */
    private String controllerName;

    /**
     * 接口url
     */
    private String url;
}
