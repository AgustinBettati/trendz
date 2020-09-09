package facultad.trendz.dto;

public class ProfileEditResponseDTO {

    private String message;

    public ProfileEditResponseDTO(String message) {
        this.message = message;
    }

    public ProfileEditResponseDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
