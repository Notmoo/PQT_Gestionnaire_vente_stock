package com.pqt.client.module.gui.ressources.components;

import com.pqt.client.module.gui.ressources.generics.displayers.IFXDisplayerComponent;
import com.pqt.client.module.gui.ressources.specifics.products.listeners.IStockComponentListener;
import com.pqt.client.module.gui.ressources.specifics.products.listeners.SimpleStockComponentFirerer;
import com.pqt.client.module.gui.ressources.strings.GUIStringTool;
import com.pqt.client.module.gui.ressources.strings.IObjectStringRenderer;
import com.pqt.core.entities.product.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryTabStockDisplayer implements IFXDisplayerComponent<Collection<Product>, IStockComponentListener>{

    private SimpleStockComponentFirerer firerer;
    private BorderPane mainPane;
    private TabPane tabPane;

    public CategoryTabStockDisplayer() {
        init();
        firerer = new SimpleStockComponentFirerer();
    }

    @Override
    public void display(Collection<Product> content) {
        final ObservableList<Tab> tabs = FXCollections.emptyObservableList();
        if(content!=null){
            List<String> categories = content.stream().map(product->product.getCategory().getName()).distinct().collect(Collectors.toList());

            for(String cat : categories){
                tabs.add(createCategoryTab(cat, content.stream().filter(p->p.getCategory().getName().equals(cat)).collect(Collectors.toList())));
            }

        }

        Platform.runLater(()->{
            tabPane.getTabs().clear();
            tabPane.getTabs().addAll(tabs);
        });
    }

    @Override
    public void addListener(IStockComponentListener l) {
        firerer.addListener(l);
    }

    @Override
    public void removeListener(IStockComponentListener l) {
        firerer.removeListener(l);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    private void init(){
        mainPane = new BorderPane();
        Label title = new Label(GUIStringTool.getCategorytabStockDisplayerTitle());
        mainPane.setTop(title);

        tabPane = new TabPane();
        mainPane.setCenter(tabPane);
    }

    private Tab createCategoryTab(String categoryName, Collection<Product> products){
        Tab tab = new Tab(categoryName);
        tab.closableProperty().setValue(false);

        ListView<Product> listView = new ListView<>();
        listView.setCellFactory(list->new ListCell<Product>(){

            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(CategoryTabStockDisplayer.getProductRenderer().render(item));
                }
            }
        });

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setEditable(false);
        listView.setOnMouseClicked(event->firerer.fireContentClickEvent(event, listView.getSelectionModel().getSelectedItem()));
        listView.setOnKeyTyped(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                firerer.fireContentClickEvent(event, listView.getSelectionModel().getSelectedItem());
                event.consume();
            }
        });

        listView.setItems(FXCollections.observableArrayList(products));

        tab.setContent(listView);
        return tab;
    }

    private static IObjectStringRenderer<Product> getProductRenderer(){
        return GUIStringTool.getProductStringRenderer();
    }
}
