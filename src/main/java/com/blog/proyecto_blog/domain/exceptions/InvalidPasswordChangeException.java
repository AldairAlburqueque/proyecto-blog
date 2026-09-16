package com.blog.proyecto_blog.domain.exceptions;

public class InvalidPasswordChangeException extends RuntimeException {

    public InvalidPasswordChangeException() {
        super("No fue posible cambiar la contraseña");
    }
}
