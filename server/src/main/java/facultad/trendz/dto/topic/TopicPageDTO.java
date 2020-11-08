package facultad.trendz.dto.topic;

import java.util.List;

public class TopicPageDTO {

    private List<TopicResponseDTO> topics;

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    public TopicPageDTO(List<TopicResponseDTO> topics, int pageNumber,int pageSize, int totalPages) {
        this.topics = topics;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public TopicPageDTO() {
    }

    public List<TopicResponseDTO> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicResponseDTO> topics) {
        this.topics = topics;
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
