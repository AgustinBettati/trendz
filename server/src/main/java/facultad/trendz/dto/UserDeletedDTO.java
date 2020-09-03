package facultad.trendz.dto;

public class UserDeletedDTO {

    private String message;

    public UserDeletedDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
