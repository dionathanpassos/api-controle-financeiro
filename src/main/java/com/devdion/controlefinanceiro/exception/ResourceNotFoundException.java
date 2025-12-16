package com.devdion.controlefinanceiro.exception;

public class ResourceNotFoundException extends BusinessException{

    public ResourceNotFoundException(String resource) {
        super(resource + "não encontrado");
    }
}
