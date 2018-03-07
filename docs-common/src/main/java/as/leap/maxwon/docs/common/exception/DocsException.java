package as.leap.maxwon.docs.common.exception;

public class DocsException extends Exception {
    private int code;

    public DocsException(Throwable cause){
        super(cause);
    }

    public DocsException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
