package facultad.trendz.dto.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentCreateDTO {

    @Size(max = 10000, message = "Comment must have as much as 10000 characters")
    @NotNull(message = "Comment cannot be empty")
    private String content;

    public CommentCreateDTO(String content) {
        this.content = content;
    }

    public CommentCreateDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
