package facultad.trendz.dto;

public class JwtResponseDTO {

    private static final String type = "Bearer";
    private String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }

    public JwtResponseDTO() {
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
