package com.pqt.client.gui.modules.stock_screen;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.ressources.strings.IObjectStringRenderer;
import com.pqt.core.entities.product.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class StockScreenView implements IFXComponent {

    private StockScreenController ctrl;
    private Pane mainPane;
    private TableView<Product> stockTableView;

    StockScreenView(StockScreenController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new Pane();
        mainPane.getStyleClass().add("main-module-pane");
        BorderPane mainPaneContent = new BorderPane();
        mainPane.getChildren().add(mainPaneContent);
        mainPaneContent.prefWidthProperty().bind(mainPane.widthProperty());
        mainPaneContent.prefHeightProperty().bind(mainPane.heightProperty());

        Button addProductButton = new Button(GUIStringTool.getAddButtonLabel());
        addProductButton.setOnMouseClicked(event -> ctrl.onAddProductRequest());
        Button detailProductButton = new Button(GUIStringTool.getDetailButtonLabel());
        detailProductButton.setOnMouseClicked(event -> ctrl.onDetailProductRequest());
        Button removeProductButton = new Button(GUIStringTool.getRemoveButtonLabel());
        removeProductButton.setOnMouseClicked(event -> ctrl.onDeleteProductRequest());
        Button refreshProductButton = new Button(GUIStringTool.getRefreshButtonLabel());
        refreshProductButton.setOnMouseClicked(event -> ctrl.onRefreshProductsRequest());

        ToolBar actionToolbar = new ToolBar();
        actionToolbar.getItems().addAll(addProductButton, detailProductButton, removeProductButton, refreshProductButton);

        HBox mainPaneTopContent = new HBox();
        HBox separator = new HBox();
        mainPaneTopContent.getChildren().addAll(separator, actionToolbar);
        HBox.setHgrow(separator, Priority.ALWAYS);
        mainPaneContent.setTop(mainPaneTopContent);

        stockTableView = new TableView<>();
        stockTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        stockTableView.setRowFactory(tableView->{
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                    ctrl.getProductActivationListener().onProductActivated(row.getItem());
            });
            row.setOnKeyTyped(event -> {
                if (event.getCode().equals(KeyCode.ENTER))
                    ctrl.getProductActivationListener().onProductActivated(row.getItem());
            });
            return row;
        });
        stockTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        List<TableColumn<Product, ?>> columns = new ArrayList<>();

        columns.add(createNewTableColumn(String.class,
                GUIStringTool.getProductNameColumnHeader(),
                param -> new SimpleStringProperty(param.getValue().getName()),
                null
        ));
        columns.add(createNewTableColumn(String.class,
                GUIStringTool.getProductCategoryColumnHeader(),
                param -> new SimpleStringProperty(param.getValue().getCategory().getName()),
                null
        ));
        columns.add(createNewTableColumn(Integer.class,
                GUIStringTool.getProductAmountRemainingColumnHeader(),
                param -> new SimpleIntegerProperty(param.getValue().getAmountRemaining()).asObject(),
                null
        ));
        columns.add(createNewTableColumn(Integer.class,
                GUIStringTool.getProductAmountSoldColumnHeader(),
                param -> new SimpleIntegerProperty(param.getValue().getAmountSold()).asObject(),
                null
        ));
        columns.add(createNewTableColumn(Double.class,
                GUIStringTool.getProductPriceColumnHeader(),
                param -> new SimpleDoubleProperty(param.getValue().getAmountSold()).asObject(),
                GUIStringTool.getPriceRenderer()
        ));
        columns.add(createNewTableColumn(Boolean.class,
                GUIStringTool.getProductIsSellableColumnHeader(),
                param -> new SimpleBooleanProperty(param.getValue().isSellable()),
                GUIStringTool.getBooleanRenderer()
        ));

        stockTableView.getColumns().addAll(columns);
        mainPaneContent.setCenter(stockTableView);
    }

    private <T> TableColumn<Product, T> createNewTableColumn(Class<T> clazz,
                                                             String header,
                                                             Callback<TableColumn.CellDataFeatures<Product, T>, ObservableValue<T>> cellValueFactory,
                                                             IObjectStringRenderer<T> renderer){
        TableColumn<Product, T> column = new TableColumn<>();
        if(header!=null)
            column.setText(header);
        if(cellValueFactory!=null)
            column.setCellValueFactory(cellValueFactory);
        if(renderer!=null)
            column.setCellFactory(table -> new TableCell<Product, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(renderer.render(item));
                        }
                    }
                }
            );

        return column;
    }

    void display(Collection<Product> productCollection){
        Platform.runLater(()->{
            this.stockTableView.getItems().clear();
            this.stockTableView.getItems().addAll(productCollection);
        });
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    Product getSelectedProduct() {
        return stockTableView.getSelectionModel().getSelectedItem();
    }
}
