package show.lmm.nanidoc.model;

import java.util.ArrayList;
import java.util.List;

public class ModelInfo {

    /**
     * 标题
     * */
    private String title;

    /**
     * 名称
     * */
    private String name;

    /**
     * 字段列表
     * */
    private List<TypeInfo> fieldList = new ArrayList<>();

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

    public List<TypeInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<TypeInfo> fieldList) {
        this.fieldList = fieldList;
    }
}
