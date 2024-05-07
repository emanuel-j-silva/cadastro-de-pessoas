package com.example.application.View.listperson;

import com.example.application.Model.Pessoa;
import com.example.application.View.MainLayout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


@PageTitle("List Person")
@Route(value = "list-person", layout = MainLayout.class)
@Uses(Icon.class)
public class ListPersonView extends Composite<VerticalLayout> {

    public ListPersonView() {
        H2 h2 = new H2();
        ListBox<Pessoa> pessoaListBox = new ListBox<>();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button btnExcluir = new Button();
        Button btnExibir = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h2.setText("Pessoas cadastradas");
        h2.setWidth("max-content");
        pessoaListBox.setWidth("min-content");
        setListBoxData(pessoaListBox);

        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        btnExcluir.setText("Excluir");
        btnExcluir.setWidth("min-content");
        btnExcluir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnExibir.setText("Exibir");
        btnExibir.setWidth("min-content");
        getContent().add(h2);
        getContent().add(pessoaListBox);
        getContent().add(layoutRow);
        layoutRow.add(btnExcluir);
        layoutRow.add(btnExibir);

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");
        dialog.setHeaderTitle("Informações de cadastro");
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);
        getContent().add(dialog);



        btnExcluir.addClickListener(buttonClickEvent -> {
            Pessoa selected = pessoaListBox.getValue();
            if (selected != null){
                excluirPessoa(pessoaListBox.getValue());
                pessoaListBox.getListDataView().removeItem(selected);
                pessoaListBox.getListDataView().refreshAll();
                Notification.show("Pessoa deletada com sucesso.",
                                3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }else{
                System.out.println("Nenhuma pessoa foi selecionada");
                Notification.show("Falha ao excluir pessoa.",
                                3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        btnExibir.addClickListener(buttonClickEvent -> {
           Pessoa selected = pessoaListBox.getValue();
           if (selected != null){
               dialog.removeAll();
               VerticalLayout dialogLayout = createDialogLayout(dialog,selected);
               dialog.add(dialogLayout);
               dialog.open();
           }else{
               System.out.println("Nenhuma pessoa foi selecionada");
           }
        });

    }

    private VerticalLayout createDialogLayout(Dialog dialog,Pessoa pessoa) {
        TextField nameField = new TextField("Nome", pessoa.getNome(),
                "Full name");
        nameField.setReadOnly(true);
        nameField.getStyle().set("padding-top", "0");

        TextField sexoField = new TextField("Sexo", String.valueOf(pessoa.getSexo()),
                "Sexo");
        sexoField.setReadOnly(true);
        sexoField.getStyle().set("padding-top","0");

        String dataNascValue = pessoa.getDataNascimento()
                .format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
        TextField dataNascField = new TextField("Data de Nascimento", dataNascValue,
                "DataNasc");
        dataNascField.setReadOnly(true);
        dataNascField.getStyle().set("padding-top","0");

        String voluntarioValue;
        if (pessoa.isVoluntario()) voluntarioValue = "SIM";
        else voluntarioValue = "NÃO";
        TextField voluntarioField = new TextField("Voluntário", voluntarioValue,
                "Voluntario");
        voluntarioField.setReadOnly(true);
        voluntarioField.getStyle().set("padding-top","0");

        VerticalLayout fieldLayout = new VerticalLayout(nameField,sexoField,
                dataNascField, voluntarioField);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return fieldLayout;
    }

    private List<Pessoa> fetchPessoasFromAPI() {
        try {
            var client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/pessoas"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Pessoa>>() {
                });
            } else {
                System.err.println("Erro ao obter pessoas. Status code: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private void excluirPessoa(Pessoa pessoa){
        try{
            var client = HttpClient.newHttpClient();
            String uri = "http://localhost:8080/api/pessoas/" + pessoa.getId();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200){
                            System.out.println("Pessoa excluída com sucesso.");
                        }else{
                            System.out.println("Falha ao excluir pessoa. Status code: " + response.statusCode());
                        }
                    }).exceptionally(e ->{
                        System.out.println("Erro ao excluir pessoa: " + e.getMessage());
                        return null;
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setListBoxData(ListBox<Pessoa> listBox) {
        List<Pessoa> listPessoas = fetchPessoasFromAPI();
        listBox.setItems(listPessoas);

        listBox.setItemLabelGenerator(item -> ((Pessoa) item).getNome());
    }
}
