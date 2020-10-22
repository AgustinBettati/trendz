package facultad.trendz.dto.user;

import facultad.trendz.model.Role;

public class UserResponseDTO {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private boolean deleted;

    public UserResponseDTO(Long id, String email, String username, Role role, boolean deleted) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.deleted = deleted;
    }

    public UserResponseDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}