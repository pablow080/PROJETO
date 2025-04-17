package org.desafioestagio.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.desafioestagio.wicket.dto.ClienteDTO;
import org.desafioestagio.wicket.dto.EnderecoDTO;
import org.desafioestagio.wicket.service.ClienteRestClient;

import java.util.List;

public class ClientePage extends WebPage {

    public ClientePage() {

        ClienteRestClient clienteRestClient = new ClienteRestClient();
        List<ClienteDTO> clientes = clienteRestClient.listarTodos();

        System.out.println("Clientes na página: " + clientes); // Debug

        // Criação de um DataProvider baseado na lista obtida do serviço REST
        ListDataProvider<ClienteDTO> provider = new ListDataProvider<>(clientes);

        // DataView para exibir os dados na tabela
        DataView<ClienteDTO> dataView = new DataView<>("clienteRow", provider) {
            @Override
            protected void populateItem(Item<ClienteDTO> item) {
                ClienteDTO cliente = item.getModelObject();
                item.add(new Label("id", Model.of(cliente.getId())));
                item.add(new Label("tipo", Model.of(
                        cliente.getTipoPessoa() != null && cliente.getTipoPessoa().equals("FISICA") ? "Física" : "Jurídica"
                )));
                item.add(new Label("cpfCnpj", Model.of(cliente.getCpfCnpj())));

                item.add(new Label("nomeRazaoSocial", Model.of(
                        cliente.getTipoPessoa() != null && cliente.getTipoPessoa().equals("FISICA")
                                ? cliente.getNome() // Para Pessoa Física
                                : cliente.getRazaoSocial() != null ? cliente.getRazaoSocial() : "" // Para Pessoa Jurídica
                )));

                item.add(new Label("rgInscricaoEstadual", Model.of(
                        "FISICA".equals(cliente.getTipoPessoa()) ? cliente.getRg() : cliente.getInscricaoEstadual()
                )));

                item.add(new Label("dataNascimentoCriacao", Model.of(
                        cliente.getTipoPessoa() != null && cliente.getTipoPessoa().equals("FISICA")
                                ? cliente.getDataNascimento() != null ? cliente.getDataNascimento() : "Data não disponível" // Para Pessoa Física
                                : cliente.getDataCriacao() != null ? cliente.getDataCriacao() : "Data não disponível" // Para Pessoa Jurídica
                )));

                item.add(new Label("email", Model.of(cliente.getEmail())));

                // Endereço (pegando o primeiro)
                EnderecoDTO endereco = cliente.getEndereco();
                item.add(new Label("telefone", Model.of(endereco != null ? endereco.getTelefone() : "")));
                item.add(new Label("cep", Model.of(endereco != null ? endereco.getCep() : "")));

                item.add(new Label("ativo", Model.of(cliente.isAtivo() ? "Ativo" : "Inativo")));

                // Links
                PageParameters params = new PageParameters();
                params.add("id", cliente.getId());
                item.add(new BookmarkablePageLink<>("editLink", ClienteEditPage.class, params));

                item.add(new AjaxLink<Void>("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        ClienteRestClient.excluir(cliente.getId());
                        setResponsePage(ClientePage.class); // ou atualize via Ajax
                    }
                });
            }
        };

        add(dataView);

        // Link para criar um novo cliente
        add(new BookmarkablePageLink<>("createLink", ClienteEditPage.class));
    }
}