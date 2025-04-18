package org.desafioestagio.backend.controller;

import org.desafioestagio.backend.exception.ResourceNotFoundException;
import org.desafioestagio.backend.model.Cliente;
import org.desafioestagio.backend.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        // Certifique-se de que os endereços são enviados corretamente
        if (cliente.getEnderecos() != null && !cliente.getEnderecos().isEmpty()) {
            Cliente novoCliente = clienteService.salvar(cliente);
            return ResponseEntity.created(URI.create("/api/clientes/" + novoCliente.getId())).body(novoCliente);
        } else {
            return ResponseEntity.badRequest().build();  // Se não houver endereços, retornamos erro de requisição
        }
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    @GetMapping("/buscar")
    public List<Cliente> buscarClientes(@RequestParam(required = false) String nome,
                                        @RequestParam(required = false) String razaoSocial,
                                        @RequestParam(required = false) String cpfCnpj,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String rg,
                                        @RequestParam(required = false) String inscricaoEstadual) {
        return clienteService.buscarClientes(nome, razaoSocial, cpfCnpj, email, rg, inscricaoEstadual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            // Atualiza cliente e endereços
            return ResponseEntity.ok(clienteService.atualizar(id, cliente));
        } catch (ResourceNotFoundException e) {
            System.out.println("Cliente não encontrado com id: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            clienteService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
