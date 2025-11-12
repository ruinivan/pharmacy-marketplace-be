package pharmacymarketplace.exceptions;

import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handler para 404 Not Found (Como o seu)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handler para 409 Conflict (Como o seu)
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                new Date(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // Handler para 401 Unauthorized (Falha de Autenticação/Login)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Falha na autenticação: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    // Handler para 403 Forbidden (Falha de Autorização - @PreAuthorize)
    @ExceptionHandler(AccessDeniedException.class) // [90]
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                new Date(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Acesso negado. Você não tem permissão para executar esta ação.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    // NOVO: Handler para 400 Bad Request (Falhas de Validação @Valid) [96, 97, 98]
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiValidationErrorResponse body = new ApiValidationErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro de validação. Verifique os campos.",
                request.getDescription(false).replace("uri=", ""),
                errors
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handler genérico 500 (Como o seu)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Record DTOs para Respostas de Erro ---
    private record ApiErrorResponse(Date timestamp, int status, String error, String message, String path) {}

    private record ApiValidationErrorResponse(Date timestamp, int status, String error, String message, String path, Map<String, String> validationErrors) {}
}