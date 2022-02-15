package show.lmm.nanidoc.demo.model.view.response;

/**
 * 基础response
 * */
public class BaseResponse {

    /**
     * 响应code
     * */
    private int code = 200;

    /**
     * 错误信息
     * */
    private String errorMessage;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
