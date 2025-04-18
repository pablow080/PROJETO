package org.desafioestagio.backend.service;

import org.desafioestagio.backend.exception.BusinessException;
import org.desafioestagio.backend.exception.ResourceNotFoundException;
import org.desafioestagio.backend.filtro.ClienteSpecification;
import org.desafioestagio.backend.model.Cliente;
import org.desafioestagio.backend.model.Endereco; // Importando o modelo Endereco
import org.desafioestagio.backend.repository.ClienteRepository;
import org.desafioestagio.backend.repository.EnderecoRepository; // Importando o repositório Endereco
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository; // Adicionando o repositório de Endereços

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        if (cliente.getEnderecos() == null || cliente.getEnderecos().isEmpty()) {
            throw new BusinessException("O cliente deve ter ao menos um endereço");
        }

        // Salvar cliente primeiro
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Agora, associar o cliente_id aos endereços e salvar os endereços
        for (Endereco endereco : cliente.getEnderecos()) {
            endereco.setCliente(clienteSalvo); // Associar cliente ao endereço
            enderecoRepository.save(endereco); // Salvar endereço
        }

        return clienteSalvo; // Retornar o cliente salvo
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public List<Cliente> buscarClientes(String nome, String razaoSocial, String cpfCnpj, String email, String rg, String inscricaoEstadual) {
        Specification<Cliente> spec = Specification.where(ClienteSpecification.nomeLike(nome))
                .and(ClienteSpecification.razaoSocialLike(razaoSocial))
                .and(ClienteSpecification.cpfCnpjLike(cpfCnpj))
                .and(ClienteSpecification.emailLike(email))
                .and(ClienteSpecification.rgLike(rg))
                .and(ClienteSpecification.inscricaoEstadualLike(inscricaoEstadual));

        return clienteRepository.findAll(spec);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    // Atualizar os campos do cliente
                    cliente.setTipoPessoa(clienteAtualizado.getTipoPessoa());
                    cliente.setCpfCnpj(clienteAtualizado.getCpfCnpj());
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setRazaoSocial(clienteAtualizado.getRazaoSocial());
                    cliente.setRg(clienteAtualizado.getRg());
                    cliente.setInscricaoEstadual(clienteAtualizado.getInscricaoEstadual());
                    cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
                    cliente.setDataCriacao(clienteAtualizado.getDataCriacao());
                    cliente.setEmail(clienteAtualizado.getEmail());
                    cliente.setAtivo(clienteAtualizado.isAtivo());

                    // Atualizar endereços, se necessário
                    if (clienteAtualizado.getEnderecos() != null) {
                        for (Endereco endereco : clienteAtualizado.getEnderecos()) {
                            endereco.setCliente(cliente); // Associar o cliente ao endereço
                            enderecoRepository.save(endereco); // Salvar os endereços
                        }
                    }

                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
