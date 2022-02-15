package show.lmm.nanidoc.demo.controller.basic;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import show.lmm.nanidoc.demo.model.view.request.app.GetAppListRequest;
import show.lmm.nanidoc.demo.model.view.response.app.GetAppListResponse;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/basic/app")
public class AppController {

    /**
     * 查询应用列表
     *
     * @param requestInfo 搜索条件
     * @return
     */
    @PostMapping("/list")
    public Mono<GetAppListResponse> getAppList(@RequestBody @Validated GetAppListRequest requestInfo) {
        return Mono.create((res) -> res.success(new GetAppListResponse()));
    }
}
