package facultad.trendz.controller;

import facultad.trendz.dto.MessageResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public interface ControllerUtils {

    default ResponseEntity<Object> getInvalidDTOResponse(BindingResult bindingResult) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        MessageResponseDTO body = new MessageResponseDTO(error);
        return new ResponseEntity<>(body, status);
    }
}
