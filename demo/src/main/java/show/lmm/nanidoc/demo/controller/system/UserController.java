package show.lmm.nanidoc.demo.controller.system;

import show.lmm.nanidoc.demo.model.view.request.user.GetUserInfoRequest;
import show.lmm.nanidoc.demo.model.view.request.user.UserLoginRequest;
import show.lmm.nanidoc.demo.model.view.response.user.GetUserInfoResponse;
import show.lmm.nanidoc.demo.model.view.response.user.UserLoginResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    /**
     * 用户登录
     *
     * @param requestInfo 用户登录信息
     * @return
     */
    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody @Validated UserLoginRequest requestInfo) {
        return new UserLoginResponse() {{
            setToken(UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT));
        }};
    }

    /**
     * 获得用户信息
     *
     * @param requestInfo 用户信息
     * @return
     */
    @PostMapping("/get/user_info")
    public GetUserInfoResponse getUserInfo(@RequestBody @Validated GetUserInfoRequest requestInfo) {
        return new GetUserInfoResponse() {{
            setUserName("张三");
        }};
    }
}
