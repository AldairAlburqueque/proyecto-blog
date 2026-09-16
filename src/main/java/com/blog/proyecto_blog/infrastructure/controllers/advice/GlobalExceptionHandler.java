package com.blog.proyecto_blog.infrastructure.controllers.advice;

import com.blog.proyecto_blog.domain.exceptions.EmailAlreadyInUseException;
import com.blog.proyecto_blog.domain.exceptions.InvalidCredentialsException;
import com.blog.proyecto_blog.domain.exceptions.InvalidPasswordChangeException;
import com.blog.proyecto_blog.domain.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.module.ResolutionException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException exception
    ) {
        return problem(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                exception.getMessage()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {
        return problem(
                HttpStatus.UNAUTHORIZED,
                "No autenticado",
                "Email o contraseña incorrectos"
        );
    }

    @ExceptionHandler(InvalidPasswordChangeException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPasswordChange(
            InvalidPasswordChangeException exception
    ) {
        return problem(
                HttpStatus.BAD_REQUEST,
                "No fue posible cambiar la contraseña",
                "La contraseña actual no es correcta o la nueva contraseña no es válida"
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(
            EmailAlreadyInUseException exception
    ) {
        return problem(
                HttpStatus.CONFLICT,
                "Email no disponible",
                "Ya existe una cuenta registrada con este email"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Uno o más campos no son válidos"
        );
        problem.setTitle("Error de validación");
        problem.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException exception
    ) {
        return problem(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                "Uno o más parámetros no son válidos"
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(
            DataIntegrityViolationException exception
    ) {
        log.warn("Conflicto de integridad de datos", exception);

        return problem(
                HttpStatus.CONFLICT,
                "Conflicto de datos",
                "La operación no puede completarse por el estado actual de los datos"
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(
            AccessDeniedException exception
    ) {
        return problem(
                HttpStatus.FORBIDDEN,
                "Acceso denegado",
                "No tienes permisos para realizar esta acción"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(
            Exception exception
    ) {
        log.error("Error no controlado", exception);

        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                "Ocurrió un error inesperado"
        );
    }

    private ResponseEntity<ProblemDetail> problem(
            HttpStatus status,
            String title,
            String detail
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status,
                detail
        );
        problem.setTitle(title);

        return ResponseEntity.status(status).body(problem);
    }
}