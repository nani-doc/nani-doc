package show.lmm.nanidoc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型类型model
 *
 * @author 刘明明
 * @date 2022-01-17 11:36
 */
@Data
public class GenericType {

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 子节点列表
     */
    private List<GenericType> children = new ArrayList<>();
}
