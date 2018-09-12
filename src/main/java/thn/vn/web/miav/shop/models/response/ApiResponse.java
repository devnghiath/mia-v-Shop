package thn.vn.web.miav.shop.models.response;

public class ApiResponse {
    public ApiResponse(ErrorResponse errorResponse,Object data){
        this.error = errorResponse;
        this.data = data;
    }
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    private ErrorResponse error;
}
