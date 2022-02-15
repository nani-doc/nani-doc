package show.lmm.nanidoc.model;

import java.util.List;

public class MethodItem {

    /**
     * 标题
     * */
    private String title;

    /**
     * 请求url
     * */
    private String url;

    /**
     * 请求类型
     * */
    private String contentType = "application/json";

    /**
     * 请求参数typescript
     * */
    private List<TypeInfo> requestParamList;

    /**
     * 返回值typescript
     * */
    private List<TypeInfo> responseTypeList;

    /**
     * 请求方法
     * */
    private RequestMethod requestMethod;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<TypeInfo> getRequestParamList() {
        return requestParamList;
    }

    public void setRequestParamList(List<TypeInfo> requestParamList) {
        this.requestParamList = requestParamList;
    }

    public List<TypeInfo> getResponseTypeList() {
        return responseTypeList;
    }

    public void setResponseTypeList(List<TypeInfo> responseTypeList) {
        this.responseTypeList = responseTypeList;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }
}
