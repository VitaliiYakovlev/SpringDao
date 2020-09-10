package springlecture.part3.exception;

import lombok.Getter;
import springlecture.part3.dto.ErrorDto;

import java.util.Collections;
import java.util.List;

public class UnprocessableEntityException extends RuntimeException {

    @Getter
    private final List<ErrorDto> errors;

    public UnprocessableEntityException(ErrorDto error) {
        this.errors = Collections.singletonList(error);
    }

}
