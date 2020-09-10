package springlecture.part3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springlecture.part3.controller.MusicController;
import springlecture.part3.dto.ErrorDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice(basePackageClasses = MusicController.class)
public class MusicControllerAdvice {

    @ExceptionHandler(value = InvalidInputDataException.class)
    public ResponseEntity<List<ErrorDto>> handleInvalidParams(HttpServletRequest request, Exception ex) {
        return new ResponseEntity<>(((InvalidInputDataException) ex).getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    public ResponseEntity<List<ErrorDto>> handleUnprocessableEntity(HttpServletRequest request, Exception ex) {
        return new ResponseEntity<>(((UnprocessableEntityException) ex).getErrors(), HttpStatus.BAD_REQUEST);
    }

}
