package org.desafioestagio.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando ocorre uma violação das regras de negócio.
 *
 * Esta exceção automaticamente retorna um status HTTP 400 (BAD REQUEST)
 * quando lançada em um controller Spring.
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    /**
     * -- GETTER --
     *
     */
    private String errorCode;
    /**
     * -- GETTER --
     *
     */
    private Object[] args;

    /**
     * Constrói uma nova exceção com a mensagem padrão.
     */
    public BusinessException() {
        super("Violação de regra de negócio");
    }

    /**
     * Constrói uma nova exceção com a mensagem especificada.
     *
     * @param message a mensagem detalhando a exceção
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     *
     * @param message a mensagem detalhando a exceção
     * @param cause a causa da exceção
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constrói uma nova exceção com código de erro e argumentos.
     *
     * @param errorCode código do erro para internacionalização
     * @param args argumentos para formatação da mensagem
     */
    public BusinessException(String errorCode, Object... args) {
        super();
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Constrói uma nova exceção com código de erro, mensagem e argumentos.
     *
     * @param errorCode código do erro para internacionalização
     * @param message mensagem padrão
     * @param args argumentos para formatação da mensagem
     */
    public BusinessException(String errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

}