package thn.vn.web.miav.shop.models.response;

public class JwtAuthenticationResponse {
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    private String accessToken;
    private String tokenType = "Bearer";
}
