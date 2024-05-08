package com.example.application.View.personform;

import com.example.application.DTO.PessoaDTO;
import com.example.application.Model.Sexo;
import com.example.application.View.MainLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
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
public class PersonFormView extends Composite<VerticalLayout>{

    private final TextField txtNome = new TextField();
    private final DatePicker lblData = new DatePicker();
    private final ComboBox lblSexo = new ComboBox();
    private final Checkbox lblVoluntario = new Checkbox();
    WebClient webClient = WebClient.create("http://localhost:8080/api");

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

        buttonSave.addClickListener(event -> {
            PessoaDTO pessoaDTO = new PessoaDTO(txtNome.getValue(),lblData.getValue(),
                    (Sexo) lblSexo.getValue(),lblVoluntario.getValue());

            webClient.post().uri("/pessoas").bodyValue(pessoaDTO).retrieve()
                    .toBodilessEntity()
                    .subscribe(response -> {
                            showNotification("Pessoa cadastrada com sucesso",true);
                    }, error ->{
                        showNotification("Erro ao cadastrar",false);
                    });
            clearDados();
        });

        buttonCancel.addClickListener(buttonClickEvent -> clearDados());

    }

    public void clearDados(){
        txtNome.clear();
        lblData.clear();
        lblSexo.clear();
        lblVoluntario.clear();
    }

    private void showNotification(String message, boolean success){
        getUI().ifPresent(ui -> ui.access(()->{
            Notification notification = new Notification();

            notification.setDuration(3000);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.setText(message);

            if (success) {
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
            else{
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            notification.open();
        }));
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        comboBox.setItems(Sexo.values());
        comboBox.setItemLabelGenerator(item-> item.toString());
    }
}
