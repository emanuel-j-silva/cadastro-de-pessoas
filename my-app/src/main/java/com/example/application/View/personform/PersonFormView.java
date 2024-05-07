package com.example.application.View.personform;

import com.example.application.DTO.PessoaDTO;
import com.example.application.Model.Sexo;
import com.example.application.View.MainLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Person Form")
@Route(value = "person-form", layout = MainLayout.class)
@Uses(Icon.class)
public class PersonFormView extends Composite<VerticalLayout> {

    private TextField txtNome = new TextField();
    private DatePicker lblData = new DatePicker();
    private ComboBox lblSexo = new ComboBox();
    private Checkbox lblVoluntario = new Checkbox();

    public PersonFormView() {

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonSave = new Button();
        Button buttonCancel = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Informações de cadastro");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        txtNome.setLabel("Nome Completo");
        lblData.setLabel("Data de Nascimento");
        lblSexo.setLabel("Sexo");
        lblSexo.setWidth("min-content");
        setComboBoxSampleData(lblSexo);
        lblVoluntario.setLabel("Voluntário");
        lblVoluntario.setWidth("min-content");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonSave.setText("Save");
        buttonSave.setWidth("min-content");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonCancel.setText("Cancel");
        buttonCancel.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(txtNome);
        formLayout2Col.add(lblData);
        formLayout2Col.add(lblSexo);
        formLayout2Col.add(lblVoluntario);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonSave);
        layoutRow.add(buttonCancel);


        buttonSave.addClickListener(buttonClickEvent -> {
            enviaDados(txtNome.getValue(),lblData.getValue(),
                    (Sexo)lblSexo.getValue(),lblVoluntario.getValue());
            clearDados();
            Notification notification = Notification.show("Pessoa cadastrada com sucesso!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.setDuration(3000);

        });

        buttonCancel.addClickListener(buttonClickEvent -> clearDados());

    }

    public void clearDados(){
        txtNome.clear();
        lblData.clear();
        lblSexo.clear();
        lblVoluntario.clear();
    }


    private void enviaDados(String nome, LocalDate dataNascimento, Sexo sexo, Boolean voluntario){
        try{
            var client = HttpClient.newHttpClient();
            PessoaDTO pessoaDTO = new PessoaDTO(nome,dataNascimento,sexo,voluntario);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(pessoaDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/pessoas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Status Code: " + response.statusCode());
                        System.out.println("Response: " + response.body());
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void setComboBoxSampleData(ComboBox comboBox) {
        comboBox.setItems(Sexo.values());
        comboBox.setItemLabelGenerator(item-> item.toString());
    }
}
