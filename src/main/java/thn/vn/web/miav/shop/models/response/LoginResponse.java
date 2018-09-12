package thn.vn.web.miav.shop.models.response;

public class LoginResponse {
    public LoginResponse(String userName,String tokenId,String email){
        this.userName = userName;
        this.tokenId = tokenId;
        this.email = email;
    }
    private String userName;
    private String tokenId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
}
