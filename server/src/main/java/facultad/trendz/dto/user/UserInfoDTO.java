package facultad.trendz.dto.user;

import facultad.trendz.dto.post.SimplePostResponseDTO;

import java.util.List;

public class UserInfoDTO {
    private UserResponseDTO userInfo;

    private List<SimplePostResponseDTO> posts;

    public UserInfoDTO() {
    }

    public UserInfoDTO(UserResponseDTO userInfo, List<SimplePostResponseDTO> posts) {
        this.userInfo = userInfo;
        this.posts = posts;
    }

    public UserResponseDTO getuserInfo() {
        return userInfo;
    }

    public void setuserInfo(UserResponseDTO userInfo) {
        this.userInfo = userInfo;
    }

    public List<SimplePostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<SimplePostResponseDTO> posts) {
        this.posts = posts;
    }
}
