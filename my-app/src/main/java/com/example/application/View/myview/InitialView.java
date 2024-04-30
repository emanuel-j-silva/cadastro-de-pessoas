package com.example.application.View.myview;

import com.example.application.View.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

@PageTitle("Initial View")
@Route(value = "")
@RouteAlias(value = "home", layout = MainLayout.class)
@Uses(Icon.class)
public class InitialView extends Composite<VerticalLayout> {

    public InitialView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h1 = new H1();
        H2 h2 = new H2();
        Hr hr = new Hr();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonCadastrar = new Button();
        Button buttonListar = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Volunteer Registration System");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h1);
        h1.setWidth("max-content");
        h2.setText("VRS");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h2);
        h2.setWidth("max-content");
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.addClassName(Padding.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonCadastrar.setText("Cadastrar");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, buttonCadastrar);
        buttonCadastrar.setWidth("min-content");
        buttonCadastrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonListar.setText("Listar");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, buttonListar);
        buttonListar.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h1);
        layoutColumn2.add(h2);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow2);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonCadastrar);
        layoutRow.add(buttonListar);

        buttonCadastrar.addClickListener(buttonClickEvent -> {
            buttonClickEvent.getSource().getUI().ifPresent(ui ->
                ui.navigate("person-form")
            );
        });

        buttonListar.addClickListener(buttonClickEvent -> {
            buttonClickEvent.getSource().getUI().ifPresent(ui ->
                    ui.navigate("list-person")
            );
        });
    }
}
