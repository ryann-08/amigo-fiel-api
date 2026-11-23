package com.amigofiel.api.exception;
//para o erro 409
// "extends RuntimeException" transforma a classe em uma exceção real do Java
public class BusinessRuleException extends RuntimeException {

    // Construtor que permite passar a mensagem de erro entre parênteses
    public BusinessRuleException(String message) {
        super(message);
    }
}