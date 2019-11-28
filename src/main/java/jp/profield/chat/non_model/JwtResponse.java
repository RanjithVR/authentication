package jp.profield.chat.non_model;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String status;
    private String userId;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JwtResponse(String accessToken) {
        this.token = accessToken;
    }

    public JwtResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}