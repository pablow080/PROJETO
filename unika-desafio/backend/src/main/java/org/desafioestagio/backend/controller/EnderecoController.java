package org.desafioestagio.backend.controller;

import org.desafioestagio.backend.exception.ResourceNotFoundException;
import org.desafioestagio.backend.model.Cliente;
import org.desafioestagio.backend.model.Endereco;
import org.desafioestagio.backend.repository.ClienteRepository;
import org.desafioestagio.backend.repository.EnderecoRepository;
import org.desafioestagio.backend.service.ClienteService;
import org.desafioestagio.backend.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoService enderecoService;
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<Endereco> criar(@RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.salvar(endereco);
        return ResponseEntity.ok(novoEndereco);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Endereco> listarPorCliente(@PathVariable Long clienteId) {
        return enderecoService.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscar(@PathVariable Long id) {
        return enderecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco enderecoAtualizado) {
        // Buscar o endereço existente
        Endereco enderecoExistente = enderecoRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        // Atualizar os campos do endereço
        enderecoExistente.setLogradouro(enderecoAtualizado.getLogradouro());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setCep(enderecoAtualizado.getCep());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setTelefone(enderecoAtualizado.getTelefone());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setComplemento(enderecoAtualizado.getComplemento());
        enderecoExistente.setPrincipal(enderecoAtualizado.isPrincipal());

        // Buscar o cliente com id "7" e associar ao endereço
        Cliente cliente = clienteRepository.findById(enderecoAtualizado.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        enderecoExistente.setCliente(cliente); // Associar o cliente

        // Salvar o endereço atualizado
        Endereco enderecoSalvo = enderecoRepository.save(enderecoExistente);

        return ResponseEntity.ok(enderecoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
