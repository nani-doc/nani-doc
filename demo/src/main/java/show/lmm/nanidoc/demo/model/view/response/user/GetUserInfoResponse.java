package show.lmm.nanidoc.demo.model.view.response.user;

import show.lmm.nanidoc.demo.model.view.response.BaseResponse;

/**
 * 获得用户信息response
 * */
public class GetUserInfoResponse extends BaseResponse {

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名
     * */
    private String userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
