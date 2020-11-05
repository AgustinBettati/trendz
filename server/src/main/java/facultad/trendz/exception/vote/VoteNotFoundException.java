package facultad.trendz.exception.vote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Requested vote not found")
public class VoteNotFoundException extends RuntimeException {
}
