package com.pqt.client.gui.ressources.components;

import com.pqt.client.gui.ressources.components.specifics.sale.IFXSaleDisplayerComponent;
import com.pqt.client.gui.ressources.components.specifics.sale.listeners.ISaleComponentListener;
import com.pqt.client.gui.ressources.components.specifics.sale.listeners.SimpleSaleComponentFirerer;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class CommandComposerSaleDisplayer implements IFXSaleDisplayerComponent {

    private SimpleSaleComponentFirerer firerer;
    private BorderPane mainPane;
    private ListView<Product> listView;

    private Sale sale;

    public CommandComposerSaleDisplayer() {
        firerer = new SimpleSaleComponentFirerer();
        init();
    }

    private void init() {
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("sale-displayer");

        Label title = new Label(GUIStringTool.getCommandComposerTitleTitle());
        title.setAlignment(Pos.CENTER);
        title.getStyleClass().add(GUICssTool.getTitleTextStyleClass());

        HBox topPane = new HBox();
        topPane.setFillHeight(true);
        topPane.setAlignment(Pos.CENTER);
        topPane.getChildren().add(title);

        mainPane.setTop(topPane);

        listView = new ListView<>();
        listView.setCellFactory(list->new ListCell<Product>(){
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(GUIStringTool.getSaleItemStringRenderer().render(item, sale.getProducts().get(item)));
                }
            }
        });
        listView.setEditable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setOnMouseClicked(event->firerer.fireComponentClickEvent(event, listView.getSelectionModel().getSelectedItem()));
        listView.setOnKeyTyped(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                firerer.fireComponentClickEvent(event, listView.getSelectionModel().getSelectedItem());
            }
        });
        mainPane.setCenter(listView);
    }

    @Override
    public void display(Sale content) {
        if(content ==null)
            return;

        this.sale = content;
        Platform.runLater(()->{
            this.listView.getItems().clear();
            this.listView.getItems().addAll(this.sale.getProducts().keySet());
        });
    }

    @Override
    public void addListener(ISaleComponentListener l) {
        firerer.addListener(l);
    }

    @Override
    public void removeListener(ISaleComponentListener l) {
        firerer.removeListener(l);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }
}
