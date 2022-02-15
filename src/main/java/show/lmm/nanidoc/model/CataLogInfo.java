package show.lmm.nanidoc.model;

import java.util.List;

/**
 * 目录信息
 */
public class CataLogInfo {

    /**
     * 控制器标题
     */
    private String controllerTitle;

    /**
     * 控制器名称
     */
    private String controllerName;
    /**
     * 页面名称
     */
    private List<PageInfo> pageList;

    public String getControllerTitle() {
        return controllerTitle;
    }

    public void setControllerTitle(String controllerTitle) {
        this.controllerTitle = controllerTitle;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public List<PageInfo> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageInfo> pageList) {
        this.pageList = pageList;
    }
}
