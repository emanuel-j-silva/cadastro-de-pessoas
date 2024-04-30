package com.example.application.View.listperson;

import com.example.application.Model.Pessoa;
import com.example.application.View.MainLayout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("List Person")
@Route(value = "list-person")
@Uses(Icon.class)
public class ListPersonView extends Composite<VerticalLayout> {

    public ListPersonView() {
        H2 h2 = new H2();
        ListBox<Pessoa> pessoaListBox = new ListBox<>();
        Button btnExcluir = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h2.setText("Pessoas cadastradas");
        h2.setWidth("max-content");
        pessoaListBox.setWidth("min-content");
        setListBoxData(pessoaListBox);

        btnExcluir.setText("Excluir");
        btnExcluir.setWidth("min-content");
        btnExcluir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(h2);
        getContent().add(pessoaListBox);
        getContent().add(btnExcluir);

        btnExcluir.addClickListener(buttonClickEvent -> {
            Pessoa selected = pessoaListBox.getValue();
            if (selected != null){
                excluirPessoa(pessoaListBox.getValue());
                pessoaListBox.getListDataView().removeItem(selected);
                pessoaListBox.getListDataView().refreshAll();
            }else{
                System.out.println("Nenhuma pessoa foi selecionada");
            }
        });
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
