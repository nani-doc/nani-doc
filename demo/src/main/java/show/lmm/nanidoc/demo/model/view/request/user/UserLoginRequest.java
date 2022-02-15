package show.lmm.nanidoc.demo.model.view.request.user;

/**
 * 用户登录request
 */
public class UserLoginRequest {

    /**
     * 用户名
     * */
    private String userName;

    /**
     * 密码
     * */
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
