package facultad.trendz.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class TopicCreateDTO {

    @Size(max = 40, message = "Password must have as much as 40 characters")
    @NotNull(message = "Title cannot be empty")
    private String title;

    private String description;

    private Date date;

    public TopicCreateDTO(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }
}
