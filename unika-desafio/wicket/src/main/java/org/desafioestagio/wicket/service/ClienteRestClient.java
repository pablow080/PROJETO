package org.desafioestagio.wicket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.desafioestagio.wicket.dto.ClienteDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteRestClient {

    private static final String BASE_URL = "http://localhost:8080/api/clientes"; // URL da API
    private static RestTemplate restTemplate = null;
    private static final Logger LOGGER = Logger.getLogger(ClienteRestClient.class.getName());

    public ClienteRestClient() {
        restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Registrando módulos para data/tempo
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    // Método para listar todos os clientes
    public List<ClienteDTO> listarTodos() {
        try {
            // Usando exchange para a desserialização correta da lista
            ResponseEntity<List<ClienteDTO>> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ClienteDTO>>() {} // Desserializa a lista
            );
            return response.getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes", e);
            return Collections.emptyList();
        }
    }

    // Método para buscar cliente por ID diretamente sem necessidade de PageParameters
    public ClienteDTO buscarPorId(Long id) {
        try {
            String url = BASE_URL + "/" + id;
            ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(url, ClienteDTO.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente com ID " + id, e);
            return null;
        }
    }

    // Método para salvar um novo cliente
    public boolean salvar(ClienteDTO clienteDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(BASE_URL, request, Void.class);
            return response.getStatusCode() == HttpStatus.CREATED; // Verifica se a criação foi bem-sucedida
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
            return false; // Em caso de erro, retorna false
        }
    }

    // Método para atualizar um cliente existente
    public boolean atualizar(Long id, ClienteDTO cliente) {
        return enviarRequisicao(cliente, cliente.getId());
    }

    // Método genérico para envio de requisições POST e PUT
    private boolean enviarRequisicao(ClienteDTO cliente, Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ClienteDTO> entity = new HttpEntity<>(cliente, headers);
            String url = (id != null) ? BASE_URL + "/" + id : BASE_URL;

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            if (HttpMethod.PUT == HttpMethod.POST) {
                return response.getStatusCode() == HttpStatus.CREATED;
            } else {
                return response.getStatusCode() == HttpStatus.OK;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao enviar requisição " + HttpMethod.PUT.name() + " para o cliente", e);
            return false;
        }
    }

    // Método para excluir um cliente
    public static void excluir(Long id) {
        try {
            String url = BASE_URL + "/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir cliente com ID " + id, e);
        }
    }
}