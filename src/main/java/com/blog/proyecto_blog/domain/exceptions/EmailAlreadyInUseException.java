package com.blog.proyecto_blog.domain.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException() {
        super("El email ya se encuentra registrado");
    }
}
