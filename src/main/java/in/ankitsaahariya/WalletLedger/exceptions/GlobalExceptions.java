package in.ankitsaahariya.WalletLedger.exceptions;

import in.ankitsaahariya.WalletLedger.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> UserAlreadyExists(UserAlreadyExistsException ex , HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .path(request.getServletPath())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
}
