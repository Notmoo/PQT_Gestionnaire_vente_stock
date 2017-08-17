package com.pqt.client.gui.modules.stock_screen;

import com.pqt.client.gui.modules.stock_screen.product_manager_screen.ProductManagerScreen;
import com.pqt.client.gui.modules.stock_screen.product_manager_screen.ProductManagerScreenFactory;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.ressources.strings.IObjectStringRenderer;
import com.pqt.core.entities.product.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class StockScreenView implements IFXComponent {

    private StockScreenController ctrl;
    private StackPane mainPane;
    private BorderPane mainPaneContent;
    private TableView<Product> stockTableView;
    private ProductManagerScreenFactory productManagerScreenFactory;
    private ProductManagerScreen currentDetailScreen;

    StockScreenView(StockScreenController ctrl, ProductManagerScreenFactory productManagerScreenFactory) {
        this.ctrl = ctrl;
        this.productManagerScreenFactory = productManagerScreenFactory;
        initGui();
    }

    private void initGui() {
        mainPane = new StackPane();
        mainPane.getStyleClass().add(GUICssTool.getMainModulePaneCssClass());
        mainPaneContent = new BorderPane();
        mainPane.getChildren().add(mainPaneContent);
        mainPaneContent.prefWidthProperty().bind(mainPane.widthProperty());
        mainPaneContent.prefHeightProperty().bind(mainPane.heightProperty());

        Button addProductButton = new Button(GUIStringTool.getAddButtonLabel());
        addProductButton.setOnMouseClicked(event -> ctrl.onAddProductRequest());
        Button detailProductButton = new Button(GUIStringTool.getDetailButtonLabel());
        detailProductButton.setOnMouseClicked(event -> ctrl.onDetailProductRequest());
        detailProductButton.setDisable(true);
        Button removeProductButton = new Button(GUIStringTool.getRemoveButtonLabel());
        removeProductButton.setDisable(true);
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
        stockTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)->{
            detailProductButton.setDisable(newVal==null);
            removeProductButton.setDisable(newVal==null);
        });
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
                param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject(),
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

    void switchToDetailMode(Product product) {
        if(currentDetailScreen==null){
            currentDetailScreen = productManagerScreenFactory.create(product);
            currentDetailScreen.addListener(ctrl.getDetailScreenValidationListener());
            Pane separator = new Pane();
            separator.getStyleClass().add(GUICssTool.getIntermediaryPaneStyleClass());
            Platform.runLater(()->mainPane.getChildren().addAll(separator, currentDetailScreen.getPane()));
        }
    }

    void switchToGeneralMode() {
        if(currentDetailScreen!=null){
            List<Node> toRemove = mainPane.getChildren()
                                                .stream()
                                                .filter(node->!node.equals(mainPaneContent))
                                                .collect(Collectors.toList());
            Platform.runLater(()->{
                mainPane.getChildren().removeAll(toRemove);
                currentDetailScreen = null;
            });
        }
    }

    boolean isDetailScreenCreationPossible() {
        System.out.println("test creation possible : ");
        return currentDetailScreen!=null && currentDetailScreen.isCreationPossible();
    }

    boolean hasDetailScreenInitialValue() {
        return currentDetailScreen!=null && currentDetailScreen.hasInitialValue();
    }

    Product getDetailScreenInitialValue() {
        if(currentDetailScreen!=null)
            return currentDetailScreen.getInitialValueSnapshot();
        else
            return null;
    }

    Product getDetailScreenCreation() {
        if(currentDetailScreen!=null)
            return currentDetailScreen.create();
        else
            return null;
    }
}
