package org.desafioestagio.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.desafioestagio.wicket.dto.ClienteDTO;
import org.desafioestagio.wicket.dto.EnderecoDTO;
import org.desafioestagio.wicket.service.ClienteRestClient;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ClientePage extends WebPage {

    private final List<ClienteDTO> clientes;
    private final ListDataProvider<ClienteDTO> provider;
    private final DataView<ClienteDTO> dataView;
    private TextField<String> searchField;

    public ClientePage() {
        setVersioned(true);
        ClienteRestClient clienteRestClient = new ClienteRestClient();

        clientes = clienteRestClient.listarTodos();
        provider = new ListDataProvider<>(clientes);

        dataView = new DataView<>("clienteRow", provider) {
            @Override
            protected void populateItem(Item<ClienteDTO> item) {
                ClienteDTO cliente = item.getModelObject();

                item.add(new Label("id", Model.of(cliente.getId())));
                item.add(new Label("tipo", Model.of(obterTipoPessoa(cliente))));
                item.add(new Label("cpfCnpj", Model.of(cliente.getCpfCnpj())));
                item.add(new Label("nomeRazaoSocial", Model.of(obterNomeRazaoSocial(cliente))));
                item.add(new Label("rgInscricaoEstadual", Model.of(obterRgInscricao(cliente))));
                item.add(new Label("dataNascimentoCriacao", Model.of(obterDataNascimentoCriacao(cliente))));
                item.add(new Label("email", Model.of(cliente.getEmail())));

                EnderecoDTO endereco = cliente.getEndereco();
                item.add(new Label("telefone", Model.of(endereco != null ? endereco.getTelefone() : "")));
                item.add(new Label("cep", Model.of(endereco != null ? endereco.getCep() : "")));
                item.add(new Label("ativo", Model.of(cliente.isAtivo() ? "Ativo" : "Inativo")));

                PageParameters params = new PageParameters();
                params.add("id", cliente.getId());
                item.add(new BookmarkablePageLink<>("editLink", ClientePutPage.class, params));

                item.add(new AjaxLink<Void>("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        clienteRestClient.excluir(cliente.getId());
                        clientes.remove(cliente);
                        dataView.modelChanged();
                        target.add(dataView);
                    }
                });
            }

            private String obterTipoPessoa(ClienteDTO cliente) {
                return "Física".equalsIgnoreCase(cliente.getTipoPessoa()) ? "Física" : "Jurídica";
            }

            private String obterNomeRazaoSocial(ClienteDTO cliente) {
                return "Física".equalsIgnoreCase(cliente.getTipoPessoa()) ?
                        Optional.ofNullable(cliente.getNome()).orElse("Nome não disponível") :
                        Optional.ofNullable(cliente.getRazaoSocial()).orElse("Razão social não disponível");
            }

            private String obterRgInscricao(ClienteDTO cliente) {
                return "Física".equalsIgnoreCase(cliente.getTipoPessoa()) ?
                        Optional.ofNullable(cliente.getRg()).orElse("RG não disponível") :
                        Optional.ofNullable(cliente.getInscricaoEstadual()).orElse("Inscrição Estadual não disponível");
            }

            private String obterDataNascimentoCriacao(ClienteDTO cliente) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return "Física".equalsIgnoreCase(cliente.getTipoPessoa()) ?
                        Optional.ofNullable(cliente.getDataNascimento()).map(formatter::format).orElse("Data de Nascimento não disponível") :
                        Optional.ofNullable(cliente.getDataCriacao()).map(formatter::format).orElse("Data de Criação não disponível");
            }
        };

        dataView.setOutputMarkupId(true); // necessário para Ajax atualizar

        add(dataView);

        // Formulário de busca
        Form<Void> searchForm = new Form<>("searchForm");
        add(searchForm);

        AjaxButton searchButton = new AjaxButton("searchButton", searchForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                String termo = searchField.getModelObject();
                List<ClienteDTO> filtrados = clienteRestClient.buscarClientesComFiltros(
                        termo, termo, termo, termo, termo, termo
                );
                clientes.clear();
                clientes.addAll(filtrados);
                dataView.modelChanged();
                target.add(dataView);
            }
        };
        searchForm.add(searchButton);

        add(new BookmarkablePageLink<>("createLink", ClientePostPage.class));
    }
}
