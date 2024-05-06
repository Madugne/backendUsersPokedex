package chunyin.backendUsersPokedex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice

public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponsePayload handleBadRequest(BadRequestException ex){
        if(ex.getErrorsList() != null) {

            String message = ex.getErrorsList().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". " ));
            return new ErrorsResponsePayload(message, LocalDateTime.now());

        } else {

            return new ErrorsResponsePayload(ex.getMessage(), LocalDateTime.now());
        }
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ErrorsResponsePayload handleNotFound(NotFoundException ex){
        return new ErrorsResponsePayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponsePayload handleGenericErrors(Exception ex){
        ex.printStackTrace();
        return new ErrorsResponsePayload("There are some server problems", LocalDateTime.now());
    }
}
