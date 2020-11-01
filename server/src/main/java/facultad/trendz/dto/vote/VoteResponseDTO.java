package facultad.trendz.dto.vote;

import javax.validation.constraints.NotNull;


public class VoteResponseDTO {


    @NotNull(message = "UserId cannot be empty")
    private Long userId;





    public VoteResponseDTO( Long userId) {
        this.userId = userId;
    }

    public VoteResponseDTO(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



}
