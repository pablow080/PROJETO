package org.desafioestagio.wicket.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.model.IModel;

@Setter
@Getter
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private boolean principal;
    private String complemento;
    private Long clienteId;  // Refere-se ao ID do cliente, pois a associação é feita pelo ID
    private boolean enderecoPrincipal;

    // Getters e Setters

}
