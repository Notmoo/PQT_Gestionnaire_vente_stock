package com.pqt.client.gui.modules.stock_screen.product_manager_screen;

import com.pqt.client.gui.ressources.components.SimpleValidator;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.javafx_override.CssEnabledGridPane;
import com.pqt.client.gui.ressources.components.generics.javafx_override.HighlightListCell;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.product.Category;
import com.pqt.core.entities.product.Product;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;

class ProductManagerScreenView implements IFXComponent {

    private ProductManagerScreenController ctrl;

    private int gridLines;
    private StackPane mainPane;
    private BorderPane mainPaneContent;
    private TextField productNameTextField,
            productAmountRemainingTextField,
            productAmountSoldTextField,
            productPriceTextField;
    private ComboBox<Category> productCategoryComboBox;
    private CheckBox productSellableCheckBox;
    private ListView<Product> productComponentsListView;

    ProductManagerScreenView(ProductManagerScreenController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new StackPane();
        mainPane.getStyleClass().add(GUICssTool.getMainModulePaneCssClass());

        mainPaneContent = new BorderPane();

        GridPane mainPaneCenterContent = new CssEnabledGridPane();
        mainPaneCenterContent.setAlignment(Pos.CENTER);
        gridLines = 0;
        NumberFormat priceFormat = NumberFormat.getNumberInstance();
        NumberFormat intFormat = NumberFormat.getIntegerInstance();

        Label productNameLabel = new Label(GUIStringTool.getProductNameLabel());
        productNameTextField = new TextField();
        productNameTextField.textProperty().addListener((obs, oldVal, newVal)->ctrl.onNameChanged(oldVal,newVal));
        addLineToGrid(mainPaneCenterContent, productNameLabel, productNameTextField);

        Label productCategoryLabel = new Label(GUIStringTool.getProductCategoryLabel());
        productCategoryComboBox = new ComboBox<>();
        productCategoryComboBox.setEditable(true);
        productCategoryComboBox.setConverter(GUIStringTool.getCategoryStringConverter());
        productCategoryComboBox.valueProperty().addListener((obs, oldVal, newVal)->ctrl.onCategoryChanged(oldVal, newVal));
        addLineToGrid(mainPaneCenterContent, productCategoryLabel, productCategoryComboBox);

        Label productAmountRemainingLabel = new Label(GUIStringTool.getProductAmountRemainingLabel());
        productAmountRemainingTextField = getNumberOnlyTextField(intFormat);
        productAmountRemainingTextField.textProperty().addListener((obs, oldVal, newVal)->{
            try{
                int oldInt = oldVal.isEmpty()?0:Integer.parseInt(oldVal);
                int newInt = newVal.isEmpty()?0:Integer.parseInt(newVal);
                ctrl.onAmountRemainingChanged(oldInt, newInt);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        });
        addLineToGrid(mainPaneCenterContent, productAmountRemainingLabel, productAmountRemainingTextField);

        Label productAmountSoldLabel = new Label(GUIStringTool.getProductAmountSoldLabel());
        productAmountSoldTextField = getNumberOnlyTextField(intFormat);
        productAmountSoldTextField.textProperty().addListener((obs, oldVal, newVal)->{
            try{
                int oldInt = oldVal.isEmpty()?0:Integer.parseInt(oldVal);
                int newInt = newVal.isEmpty()?0:Integer.parseInt(newVal);
                ctrl.onAmountSoldChanged(oldInt, newInt);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        });
        addLineToGrid(mainPaneCenterContent, productAmountSoldLabel, productAmountSoldTextField);

        Label productSellableLabel = new Label(GUIStringTool.getProductSellableLabel());
        productSellableCheckBox = new CheckBox();
        productSellableCheckBox.selectedProperty().addListener((obs, oldVal, newVal)->ctrl.onSellableStateChanged(oldVal,newVal));
        addLineToGrid(mainPaneCenterContent, productSellableLabel, productSellableCheckBox);

        Label productPriceLabel = new Label(GUIStringTool.getProductPriceLabel());
        productPriceTextField = getNumberOnlyTextField(priceFormat);
        productPriceTextField.textProperty().addListener((obs, oldVal, newVal)->{
            try{
                ctrl.onPriceChanged((oldVal.isEmpty()?-1:Double.parseDouble(oldVal)), (newVal.isEmpty()?-1:Double.parseDouble(newVal)));
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        });
        addLineToGrid(mainPaneCenterContent, productPriceLabel, productPriceTextField);

        mainPaneContent.setCenter(mainPaneCenterContent);

        VBox mainPaneRightContent = new VBox();
        mainPaneRightContent.getStyleClass().add(GUICssTool.getContainerStyleClass());
        mainPaneRightContent.setAlignment(Pos.CENTER);
        Label title = new Label(GUIStringTool.getComponentListTitleLabel());
        title.setAlignment(Pos.CENTER);
        title.getStyleClass().add(GUICssTool.getTitleTextStyleClass());
        productComponentsListView = new ListView<>();
        productComponentsListView.setCellFactory(listView->new HighlightListCell<Product>(){
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if(item==null || empty){
                    setText(null);
                    setGraphic(null);
                }else{
                    setText(GUIStringTool.getSimpleProductStringRenderer().render(item));
                    if(ctrl.isProductHighlighted(item)){
                        if(!isHightlighted()) {
                            setHighLight(true);
                            applyCss();
                        }
                    }else {
                        if (isHightlighted()) {
                            setHighLight(false);
                            applyCss();
                        }
                    }
                }
            }
        });
        productComponentsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productComponentsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)->{
            this.ctrl.getProductComponentSelectionListener().changed(obs, oldValue, newValue);
            if(newValue!=null)
                Platform.runLater(()->productComponentsListView.getSelectionModel().clearSelection(productComponentsListView.getSelectionModel().getSelectedIndex()));
        });

        mainPaneRightContent.getChildren().addAll(title, productComponentsListView);
        mainPaneContent.setRight(mainPaneRightContent);

        HBox mainPaneBottomContent = new HBox();
        HBox separator = new HBox();
        SimpleValidator validator = new SimpleValidator();
        validator.addListener(ctrl.getValidatorListener());
        mainPaneBottomContent.getChildren().addAll(separator, validator.getPane());
        HBox.setHgrow(separator, Priority.ALWAYS);

        mainPaneContent.setBottom(mainPaneBottomContent);
        mainPane.getChildren().add(mainPaneContent);
    }

    private void addLineToGrid(GridPane grid, Node... nodes){
        gridLines++;
        int columnIndex = 0;
        for(Node node : nodes){
            grid.add(node, columnIndex, gridLines);
            columnIndex++;
        }
    }

    private TextField getNumberOnlyTextField(NumberFormat format){
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldValue, newValue)->{
            if(!newValue.matches("^[-+]?[0-9]+[.,]?[0-9]*$"))
                Platform.runLater(()->textField.setText(oldValue));
        });
        return textField;
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    void setProduct(Product product){
        if(product!=null){
            Platform.runLater(()->{
                productNameTextField.setText(product.getName());
                productCategoryComboBox.setValue(product.getCategory());
                productAmountRemainingTextField.setText(Integer.toString(product.getAmountRemaining()));
                productAmountRemainingTextField.setEditable(product.getComponents().isEmpty());
                productAmountSoldTextField.setText(Integer.toString(product.getAmountSold()));
                productComponentsListView.getItems().clear();
                productComponentsListView.getItems().addAll(product.getComponents());
                productSellableCheckBox.setSelected(product.isSellable());
                productPriceTextField.setText(Double.toString(product.getPrice()));
            });
        }

    }

    void setCategoryCollection(Collection<Category> categoryCollection){
        Platform.runLater(()->{
            productCategoryComboBox.getItems().clear();
            productCategoryComboBox.getItems().addAll(categoryCollection);
        });
    }

    void setProductCollection(Collection<Product> productCollection){
        Platform.runLater(()->{
            productComponentsListView.getItems().clear();
            productComponentsListView.getItems().addAll(productCollection);
        });
    }
}
