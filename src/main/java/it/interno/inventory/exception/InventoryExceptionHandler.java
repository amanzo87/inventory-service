package it.interno.inventory.exception;

import it.interno.inventory.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InventoryExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralException(Exception ex) {
        ResponseDto response = new ResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null, ex.getMessage(), null);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(InventoryFallbackException.class)
    public ResponseEntity<ResponseDto> handleInventoryFallbackException(InventoryFallbackException ex) {
        ResponseDto response = new ResponseDto(
                HttpStatus.BAD_REQUEST.value(),null, ex.getMessage(), null);

        return ResponseEntity.ok(response);
    }

}
