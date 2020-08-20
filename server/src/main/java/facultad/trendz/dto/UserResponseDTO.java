package facultad.trendz.dto;

import facultad.trendz.model.Role;

public class UserResponseDTO {
    private String email;
    private String username;
    private Role role;

    public UserResponseDTO(String email, String username, Role role) {
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}