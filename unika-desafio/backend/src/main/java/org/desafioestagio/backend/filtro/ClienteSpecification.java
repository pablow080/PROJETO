package org.desafioestagio.backend.filtro;

import org.desafioestagio.backend.model.Cliente;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ClienteSpecification {

    public static Specification<Cliente> nomeLike(String nome) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(nome)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se o nome for vazio ou null
            }
            return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<Cliente> razaoSocialLike(String razaoSocial) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(razaoSocial)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se a razão social for vazia ou null
            }
            return criteriaBuilder.like(root.get("razaoSocial"), "%" + razaoSocial + "%");
        };
    }

    public static Specification<Cliente> cpfCnpjLike(String cpfCnpj) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(cpfCnpj)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se cpfCnpj for vazio ou null
            }
            return criteriaBuilder.like(root.get("cpfCnpj"), "%" + cpfCnpj + "%");
        };
    }

    public static Specification<Cliente> emailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(email)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se o email for vazio ou null
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }

    public static Specification<Cliente> rgLike(String rg) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(rg)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se o rg for vazio ou null
            }
            return criteriaBuilder.like(root.get("rg"), "%" + rg + "%");
        };
    }

    public static Specification<Cliente> inscricaoEstadualLike(String inscricaoEstadual) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(inscricaoEstadual)) {
                return criteriaBuilder.conjunction(); // Não aplica filtro se a inscrição estadual for vazia ou null
            }
            return criteriaBuilder.like(root.get("inscricaoEstadual"), "%" + inscricaoEstadual + "%");
        };
    }
}
