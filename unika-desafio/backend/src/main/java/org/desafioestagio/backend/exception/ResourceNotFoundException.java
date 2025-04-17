package org.desafioestagio.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um recurso não é encontrado no sistema.
 *
 * Esta exceção automaticamente retorna um status HTTP 404 (NOT FOUND)
 * quando lançada em um controller Spring.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constrói uma nova exceção com a mensagem padrão.
     */
    public ResourceNotFoundException() {
        super("Recurso não encontrado");
    }

    /**
     * Constrói uma nova exceção com a mensagem especificada.
     *
     * @param message a mensagem detalhando a exceção
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     *
     * @param message a mensagem detalhando a exceção
     * @param cause a causa da exceção
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constrói uma nova exceção com formatação de mensagem.
     *
     * @param resourceName o nome do recurso não encontrado (ex: "Cliente")
     * @param fieldName o nome do campo usado na busca (ex: "id")
     * @param fieldValue o valor do campo usado na busca (ex: "123")
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s: '%s'", resourceName, fieldName, fieldValue));
    }
}