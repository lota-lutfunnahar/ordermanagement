package com.assestment.orderservice.util;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionHandlerUtil extends Exception {
    public HttpStatus code;
    public String message;
}
//custom exception handler if needed
