package org.desafioestagio.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cliente {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private String tipoPessoa; // "FISICA" ou "JURIDICA"

    // Pessoa Física
    private String nome;
    private String rg;
    private LocalDate dataNascimento;

    // Pessoa Jurídica
    private String razaoSocial;
    private String inscricaoEstadual;
    private LocalDate dataCriacao;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    public Endereco getEndereco() {
        return enderecos != null && !enderecos.isEmpty() ? enderecos.get(0) : null;
    }

    public boolean isPessoaFisica() {
        return "FISICA".equalsIgnoreCase(tipoPessoa);
    }

    public boolean isPessoaJuridica() {
        return "JURIDICA".equalsIgnoreCase(tipoPessoa);
    }

    // Métodos específicos para acessar os campos corretos
    public String getNomeRazaoSocial() {
        return isPessoaFisica() ? nome : razaoSocial;
    }

    public String getRgInscricaoEstadual() {
        return isPessoaFisica() ? rg : inscricaoEstadual;
    }

    public LocalDate getDataNascimentoCriacao() {
        return isPessoaFisica() ? dataNascimento : dataCriacao;
    }
}
