package show.lmm.nanidoc.demo.model.view.response.app;

import show.lmm.nanidoc.demo.model.view.response.BaseResponse;

import java.util.List;

/**
 * 查询应用列表response
 */
public class GetAppListResponse extends BaseResponse {

    /**
     * 用户id
     */
    private List<AppInfo> appList;

    public List<AppInfo> getAppList() {
        return appList;
    }

    public void setAppList(List<AppInfo> appList) {
        this.appList = appList;
    }

    /**
     * 应用信息
     */
    public static class AppInfo {

        /**
         * 应用id
         */
        private int appId;

        /**
         * 应用名称
         */
        private String appName;

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}
