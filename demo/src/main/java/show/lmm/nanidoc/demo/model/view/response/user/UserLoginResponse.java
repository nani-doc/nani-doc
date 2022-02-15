package show.lmm.nanidoc.demo.model.view.response.user;

import show.lmm.nanidoc.demo.model.view.response.BaseResponse;

/**
 * 用户登录response
 * */
public class UserLoginResponse extends BaseResponse {

    /**
     * 用户身份token
     * */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
