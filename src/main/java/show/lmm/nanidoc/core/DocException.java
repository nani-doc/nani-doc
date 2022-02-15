package show.lmm.nanidoc.core;

/**
 * 文档异常
 */
public class DocException extends RuntimeException {

    /**
     * 文档异常构造方法
     *
     * @param message 错误信息
     */
    public DocException(String message) {
        super(message);
    }

    /**
     * 文档异常构造方法
     *
     * @param message 错误信息
     * @param cause   异常信息
     */
    public DocException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 文档异常构造方法
     *
     * @param message     错误信息
     * @param messageArgs 错误信息参数
     * @param cause       异常信息
     */
    public DocException(Throwable cause,String message, Object... messageArgs) {
        super(String.format(message, messageArgs), cause);
    }
}
