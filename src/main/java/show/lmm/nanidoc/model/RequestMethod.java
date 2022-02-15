package show.lmm.nanidoc.model;

import lombok.Data;

/**
 * 请求方法
 */
@Data
public class RequestMethod {

    /**
     * 标题
     * */
    private String title;

    /**
     * 请求方式
     * */
    private String requestMethod;

    /**
     * typeScript类名
     * */
    private String className;

    /**
     * 返回值类型
     */
    private String responseClassName;

    /**
     * 返回值泛型类型
     */
    private String responseGenericClassName;

    /**
     * 请求url
     * */
    private String url;

    /**
     * 方法名
     * */
    private String methodName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
