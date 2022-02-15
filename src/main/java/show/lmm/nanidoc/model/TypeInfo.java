package show.lmm.nanidoc.model;

import java.util.List;

/**
 * 类型信息
 */
public class TypeInfo {

    /**
     * 标题
     */
    private String title;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * */
    private String type;

    /**
     * 字段列表
     */
    private List<TypeInfo> fieldList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TypeInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<TypeInfo> fieldList) {
        this.fieldList = fieldList;
    }
}
