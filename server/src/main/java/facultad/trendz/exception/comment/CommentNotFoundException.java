package facultad.trendz.exception.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Requested comment not found")
public class CommentNotFoundException extends RuntimeException {
}
