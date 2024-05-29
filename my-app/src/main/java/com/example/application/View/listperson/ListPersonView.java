package com.example.application.View.listperson;

import com.example.application.DTO.PessoaAndEnderecoDTO;
import com.example.application.View.MainLayout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@PageTitle("List Person")
@Route(value = "list-person", layout = MainLayout.class)
@Uses(Icon.class)
public class ListPersonView extends Composite<VerticalLayout> {

    public ListPersonView() {
        H2 h2 = new H2();
        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        TextField textField = new TextField();
        Button filterIcon = new Button(VaadinIcon.FILTER.create());
        Button userIcon = new Button(VaadinIcon.USER_CARD.create());
        Button editIcon = new Button(VaadinIcon.EDIT.create());
        Button excluirIcon = new Button(VaadinIcon.BAN.create());
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        Hr hr = new Hr();

        Grid basicGrid = new Grid<PessoaAndEnderecoDTO>(PessoaAndEnderecoDTO.class);

        basicGrid.setWidth("100%");
        basicGrid.setHeight("400px");
        basicGrid.getStyle().set("flex-grow", "0");
        setGridData(basicGrid);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);
        layoutRow2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.START);
        textField.setLabel("Pesquisa por endereço");
        layoutRow2.setAlignSelf(FlexComponent.Alignment.CENTER, textField);
        textField.setWidth("min-content");
        h2.setText("Pessoas cadastradas");
        h2.setWidth("max-content");

        getContent().add(h2);
        layoutRow.add(layoutRow2);
        layoutRow2.add(textField);
        layoutRow.add(filterIcon);
        layoutRow.add(userIcon);
        layoutRow.add(editIcon);
        layoutRow.add(excluirIcon);
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        layoutColumn2.add(hr);
        layoutColumn2.add(basicGrid);

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");
        dialog.setHeaderTitle("Informações de cadastro");
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);
        getContent().add(dialog);



        excluirIcon.addClickListener(buttonClickEvent -> {
            Set<PessoaAndEnderecoDTO> selectedItems = basicGrid.getSelectedItems();
            if (selectedItems.size() == 1){
                PessoaAndEnderecoDTO selected = selectedItems.iterator().next();
                excluirPessoa(selected);
                basicGrid.getListDataView().removeItem(selected);
                basicGrid.getListDataView().refreshAll();
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

        userIcon.addClickListener(buttonClickEvent -> {
            Set<PessoaAndEnderecoDTO> selectedItems = basicGrid.getSelectedItems();
           if (!selectedItems.isEmpty()){
               PessoaAndEnderecoDTO selected = selectedItems.iterator().next();
               dialog.removeAll();
               VerticalLayout dialogLayout = createDialogLayout(dialog, selected);
               dialog.add(dialogLayout);
               dialog.open();
           }else{
               System.out.println("Nenhuma pessoa foi selecionada");
           }
        });

    }

    private VerticalLayout createDialogLayout(Dialog dialog,PessoaAndEnderecoDTO pessoa) {
        TextField nameField = new TextField("Nome", pessoa.nome(),
                "Full name");
        nameField.setReadOnly(true);
        nameField.getStyle().set("padding-top", "0");

        TextField sexoField = new TextField("Sexo", String.valueOf(pessoa.sexo()),"Sexo");
        sexoField.setReadOnly(true);
        sexoField.getStyle().set("padding-top","0");

        String dataNascValue = pessoa.dataNascimento().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
        TextField dataNascField = new TextField("Data de Nascimento", dataNascValue,
                "DataNasc");
        dataNascField.setReadOnly(true);
        dataNascField.getStyle().set("padding-top","0");

        String voluntarioValue;
        if (pessoa.voluntario()) voluntarioValue = "SIM";
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

    private List<PessoaAndEnderecoDTO> fetchPessoasAndEnderecoFromAPI() {
        try {
            var client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/pessoas-and-enderecos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<PessoaAndEnderecoDTO>>() {
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
    private void excluirPessoa(PessoaAndEnderecoDTO pessoa){
        try{
            var client = HttpClient.newHttpClient();
            String uri = "http://localhost:8080/api/pessoas/" + pessoa.id();
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

    private void setGridData(Grid<PessoaAndEnderecoDTO> grid){
        List<PessoaAndEnderecoDTO> listPessoas = fetchPessoasAndEnderecoFromAPI();
        grid.setItems(listPessoas);

        grid.addColumn(PessoaAndEnderecoDTO::id).setHeader("ID");
        grid.addColumn(PessoaAndEnderecoDTO::nome).setHeader("Nome");
        grid.addColumn(dto -> dto.dataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).setHeader("Data de Nascimento");
        grid.addColumn(PessoaAndEnderecoDTO::sexo).setHeader("Sexo");
        grid.addColumn(PessoaAndEnderecoDTO::voluntario).setHeader("Voluntário");
        grid.addColumn(PessoaAndEnderecoDTO::rua).setHeader("Rua");
        grid.addColumn(PessoaAndEnderecoDTO::numero).setHeader("Número");
        grid.addColumn(PessoaAndEnderecoDTO::bairro).setHeader("Bairro");
        grid.addColumn(PessoaAndEnderecoDTO::complemento).setHeader("Complemento");
    }
}
