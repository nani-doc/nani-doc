package show.lmm.nanidoc.model;

import lombok.Data;

/**
 * 参数信息
 *
 * @author 刘明明
 * @date 2022-01-21 16:08
 */
@Data
public class ArgInfo {

    /**
     * 参数名称
     */
    private String argName;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 示例
     */
    private String example = "";

    /**
     * 描述
     */
    private String description = "";
}
