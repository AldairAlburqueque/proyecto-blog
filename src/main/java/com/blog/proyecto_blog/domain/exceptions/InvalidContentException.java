package com.blog.proyecto_blog.domain.exceptions;

public class InvalidContentException extends RuntimeException {

    public InvalidContentException() {
        super("El contenido no contiene texto permitido");
    }
}
