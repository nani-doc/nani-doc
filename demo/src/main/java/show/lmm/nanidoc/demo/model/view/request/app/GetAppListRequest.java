package show.lmm.nanidoc.demo.model.view.request.app;

/**
 * 查询应用列表request
 */
public class GetAppListRequest {

    /**
     * 搜索条件：应用名称
     * */
    private int appName;

    public int getAppName() {
        return appName;
    }

    public void setAppName(int appName) {
        this.appName = appName;
    }
}
