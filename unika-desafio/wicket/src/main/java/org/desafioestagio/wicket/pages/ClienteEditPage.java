package org.desafioestagio.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.desafioestagio.wicket.dto.ClienteDTO;
import org.desafioestagio.wicket.service.ClienteRestClient;

import java.util.Arrays;

public class ClienteEditPage extends WebPage {

    private final ClienteDTO cliente;
    private final boolean isEditando;

    public ClienteEditPage(PageParameters parameters) {
        Long clienteId = parameters.get("id").toOptionalLong();
        ClienteRestClient clienteRestClient = new ClienteRestClient();

        if (clienteId != null) {
            cliente = clienteRestClient.buscarPorId(clienteId);
            isEditando = true;
        } else {
            cliente = new ClienteDTO();
            cliente.setTipoPessoa("FISICA");
            isEditando = false;
        }

        add(new FeedbackPanel("feedback"));

        Form<Void> form = new Form<>("form");
        add(form);

        // Tipo Pessoa
        DropDownChoice<String> tipoPessoaChoice = new DropDownChoice<>("tipoPessoa",
                Model.of(cliente.getTipoPessoa()),
                Arrays.asList("FISICA", "JURIDICA"));
        form.add(tipoPessoaChoice);

        // Containers dinâmicos
        WebMarkupContainer fisicaContainer = new WebMarkupContainer("fisicaContainer");
        WebMarkupContainer juridicaContainer = new WebMarkupContainer("juridicaContainer");

        fisicaContainer.setOutputMarkupPlaceholderTag(true);
        juridicaContainer.setOutputMarkupPlaceholderTag(true);

        form.add(fisicaContainer);
        form.add(juridicaContainer);

        // Campos comuns
        form.add(new TextField<>("cpfCnpj", Model.of(cliente.getCpfCnpj())));
        form.add(new TextField<>("email", Model.of(cliente.getEmail())));

        // Pessoa Física
        fisicaContainer.add(new TextField<>("nome", Model.of(cliente.getNome())));
        fisicaContainer.add(new TextField<>("rg", Model.of(cliente.getRg())));
        fisicaContainer.add(new DateTextField("dataNascimento", Model.of(), "yyyy-MM-dd"));

        // Pessoa Jurídica
        juridicaContainer.add(new TextField<>("razaoSocial", Model.of(cliente.getRazaoSocial())));
        juridicaContainer.add(new TextField<>("inscricaoEstadual", Model.of(cliente.getInscricaoEstadual())));
        juridicaContainer.add(new DateTextField("dataCriacao", Model.of(), "yyyy-MM-dd"));

        // Visibilidade dinâmica
        tipoPessoaChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String tipo = tipoPessoaChoice.getModelObject();
                fisicaContainer.setVisible("FISICA".equals(tipo));
                juridicaContainer.setVisible("JURIDICA".equals(tipo));
                target.add(fisicaContainer, juridicaContainer);
            }
        });

        fisicaContainer.setVisible("FISICA".equals(cliente.getTipoPessoa()));
        juridicaContainer.setVisible("JURIDICA".equals(cliente.getTipoPessoa()));

        // Botão salvar
        form.add(new Button("salvar") {
            @Override
            public void onSubmit() {
                super.onSubmit();

                // Atualiza os dados
                cliente.setTipoPessoa(tipoPessoaChoice.getModelObject());

                if ("FISICA".equals(cliente.getTipoPessoa())) {
                    cliente.setRazaoSocial(null);
                    cliente.setInscricaoEstadual(null);
                } else {
                    cliente.setNome(null);
                    cliente.setRg(null);
                }

                if (isEditando) {
                    clienteRestClient.atualizar(cliente.getId(), cliente);
                } else {
                    clienteRestClient.salvar(cliente);
                }

                setResponsePage(ClientePage.class);
            }
        });
    }
}
