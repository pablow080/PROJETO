package org.desafioestagio.wicket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.desafioestagio.wicket.dto.ClienteDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteRestClient {

    private static final String BASE_URL = "http://localhost:8080/api/clientes"; // URL da API
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = Logger.getLogger(ClienteRestClient.class.getName());

    public ClienteRestClient() {
        // Configuração do ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // <-- ESSENCIAL!

        // Configuração do conversor JSON
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Instanciação do RestTemplate com o conversor configurado
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        this.restTemplate.getMessageConverters().add(converter);
    }

    // Método para listar todos os clientes
    public List<ClienteDTO> listarTodos() {
        try {
            ResponseEntity<List<ClienteDTO>> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ClienteDTO>>() {}
            );
            List<ClienteDTO> lista = response.getBody();
            return (lista != null) ? lista : Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes", e);
            return Collections.emptyList();
        }
    }

    // Método para salvar um novo cliente
    public void salvar(ClienteDTO clienteDTO) {
        prepararEnderecos(clienteDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(BASE_URL, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Cliente salvo com sucesso.");
            } else {
                System.out.println("Falha ao salvar cliente. Código de status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
        }
    }

    // Método auxiliar para vincular cliente aos endereços
    private void prepararEnderecos(ClienteDTO clienteDTO) {
        if (clienteDTO.getEnderecos() != null) {
            clienteDTO.getEnderecos().forEach(endereco -> {
                endereco.setClienteDTO(clienteDTO);
                endereco.setClienteId(clienteDTO.getId());
            });
        }
    }

    // Método para atualizar um cliente existente
    public void atualizar(Long id, ClienteDTO clienteDTO) {
        prepararEnderecos(clienteDTO);
        enviarRequisicao(clienteDTO, id);
    }

    // Método genérico para envio de requisições POST e PUT
    private void enviarRequisicao(ClienteDTO cliente, Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ClienteDTO> entity = new HttpEntity<>(cliente, headers);
            String url = (id != null) ? BASE_URL + "/" + id : BASE_URL;

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Cliente atualizado com sucesso.");
            } else {
                System.out.println("Falha ao atualizar cliente. Código de status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao enviar requisição PUT para o cliente", e);
        }
    }

    // Método para excluir um cliente
    public void excluir(Long id) {
        try {
            String url = BASE_URL + "/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir cliente com ID " + id, e);
        }
    }

    // Novo método para buscar clientes com filtros
    public List<ClienteDTO> buscarClientesComFiltros(String nome, String razaoSocial, String cpfCnpj,
                                                     String email, String rg, String inscricaoEstadual) {
        try {
            // Monta a URL com os filtros
            StringBuilder url = new StringBuilder(BASE_URL + "/buscar?");

            if (nome != null && !nome.isEmpty()) {
                url.append("nome=").append(nome).append("&");
            }
            if (razaoSocial != null && !razaoSocial.isEmpty()) {
                url.append("razaoSocial=").append(razaoSocial).append("&");
            }
            if (cpfCnpj != null && !cpfCnpj.isEmpty()) {
                url.append("cpfCnpj=").append(cpfCnpj).append("&");
            }
            if (email != null && !email.isEmpty()) {
                url.append("email=").append(email).append("&");
            }
            if (rg != null && !rg.isEmpty()) {
                url.append("rg=").append(rg).append("&");
            }
            if (inscricaoEstadual != null && !inscricaoEstadual.isEmpty()) {
                url.append("inscricaoEstadual=").append(inscricaoEstadual).append("&");
            }

            // Remove o último '&'
            if (url.charAt(url.length() - 1) == '&') {
                url.deleteCharAt(url.length() - 1);
            }

            // Realiza a requisição GET
            ResponseEntity<List<ClienteDTO>> response = restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            List<ClienteDTO> lista = response.getBody();
            return (lista != null) ? lista : Collections.emptyList();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar clientes com filtros", e);
            return Collections.emptyList();
        }
    }

    public ClienteDTO buscarPorId(Long clienteId) {
        try {
            String url = BASE_URL + "/" + clienteId;
            ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente com ID " + clienteId, e);
            return null;
        }
    }
}
