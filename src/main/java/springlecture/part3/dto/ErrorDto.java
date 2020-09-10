package springlecture.part3.dto;

import lombok.Value;

@Value
public class ErrorDto {

    String field;
    String value;
    String message;
}
