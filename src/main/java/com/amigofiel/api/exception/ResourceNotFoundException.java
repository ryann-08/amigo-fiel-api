package com.amigofiel.api.exception;

public class ResourceNotFoundException extends RuntimeException {

    //erro 404 nao encontrado
    //Permite criar a exceção informando qual ID ou recurso não foi localizado
    public ResourceNotFoundException(String message) {
        super(message);
    }
}