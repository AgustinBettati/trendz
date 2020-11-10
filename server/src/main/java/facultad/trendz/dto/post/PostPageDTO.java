package facultad.trendz.dto.post;

import java.util.List;

public class PostPageDTO {

    private List<SimplePostResponseDTO> posts;

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    public PostPageDTO(List<SimplePostResponseDTO> posts, int pageNumber, int pageSize, int totalPages) {
        this.posts = posts;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public PostPageDTO() {
    }

    public List<SimplePostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<SimplePostResponseDTO> posts) {
        this.posts = posts;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
