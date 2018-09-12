package thn.vn.web.miav.shop.models.response;

public class ErrorResponse {
    public ErrorResponse(int errorCode,String message){
        this.errorCode = errorCode;
        this.message = message;
    }
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
