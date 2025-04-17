package org.desafioestagio.wicket.dto;

import lombok.Getter;
import lombok.Setter;
import org.desafioestagio.wicket.dto.EnderecoDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// ClienteDTO.java
@Getter
@Setter
public class ClienteDTO implements Serializable {
    private Long id;
    private String cpfCnpj;
    private String email;
    private boolean ativo;
    private String tipoPessoa; // "FISICA" ou "JURIDICA"

    // Pessoa Física
    private String nome;
    private String rg;
    private LocalDate dataNascimento;

    // Pessoa Jurídica
    private String razaoSocial;
    private String inscricaoEstadual;
    private LocalDate dataCriacao;

    private List<EnderecoDTO> enderecos = new ArrayList<>();

    public EnderecoDTO getEndereco() {
        return enderecos != null && !enderecos.isEmpty() ? enderecos.get(0) : null;
    }

    public boolean isPessoaFisica() {
        return "FISICA".equalsIgnoreCase(tipoPessoa);
    }

    public boolean isPessoaJuridica() {
        return "JURIDICA".equalsIgnoreCase(tipoPessoa);
    }
}
