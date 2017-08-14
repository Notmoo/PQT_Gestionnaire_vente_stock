package com.pqt.client.gui.modules.sale_screen;

import com.pqt.client.gui.modules.sale_screen.sale_validation_screen.SaleValidationScreen;
import com.pqt.client.gui.ressources.components.CommandComposerSaleDisplayer;
import com.pqt.client.gui.ressources.components.SimpleValidator;
import com.pqt.client.gui.ressources.components.generics.javafx_override.CssEnabledGridPane;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.ressources.components.CategoryTabStockDisplayer;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;
import javafx.application.Platform;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

class SaleScreenView implements IFXComponent {

    private SaleScreenController ctrl;

    private SaleValidationScreen saleValidationScreen;
    private StackPane mainPane;
    private BorderPane mainPaneContent;

    private CategoryTabStockDisplayer stockDisplayer;
    private CommandComposerSaleDisplayer saleDisplayer;
    private TextField saleMakerAccountDisplayer;
    private ChoiceBox<Account> saleBeneficiaryAccountDisplayer;
    private ChoiceBox<SaleType> saleTypeDisplayer;
    private TextField salePriceDisplayer;
    private SimpleValidator validator;

    SaleScreenView(SaleScreenController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new StackPane();
        mainPane.getStyleClass().add("main-module-pane");

        mainPaneContent = new BorderPane();

        /*
        -----------------------CENTER PANE-----------------------
         */
        {
            mainPaneContent.prefWidthProperty().bind(mainPane.widthProperty());
            mainPaneContent.prefHeightProperty().bind(mainPane.heightProperty());

            stockDisplayer = new CategoryTabStockDisplayer();
            stockDisplayer.addListener(ctrl.getStockDisplayerListener());

            saleDisplayer = new CommandComposerSaleDisplayer();
            saleDisplayer.addListener(ctrl.getSaleDisplayerListener());

            HBox mainContentCenterPane = new HBox();
            mainContentCenterPane.getChildren().addAll(stockDisplayer.getPane(), saleDisplayer.getPane());
            mainContentCenterPane.setFillHeight(true);
            stockDisplayer.getPane().prefWidthProperty().bind(mainContentCenterPane.widthProperty().divide(2));
            saleDisplayer.getPane().prefWidthProperty().bind(mainContentCenterPane.widthProperty().divide(2));

            mainPaneContent.setCenter(mainContentCenterPane);
        }
        /*
        -----------------------BOTTOM PANE-----------------------
         */
        {
            HBox mainContentBottomPane = new HBox();
            mainContentBottomPane.setFillHeight(true);
            mainContentBottomPane.setAlignment(Pos.CENTER);
            // Sale secondary data configuration (author, beneficiary, payment type, etc...
            {
                saleMakerAccountDisplayer = new TextField();
                saleMakerAccountDisplayer.setEditable(false);
                saleMakerAccountDisplayer.setPromptText(GUIStringTool.getSaleMakerTextFieldPromptText());

                saleBeneficiaryAccountDisplayer = new ChoiceBox<>();
                saleBeneficiaryAccountDisplayer.setConverter(GUIStringTool.getAccountStringConverter());
                saleBeneficiaryAccountDisplayer.getSelectionModel()
                        .selectedItemProperty()
                        .addListener((observable, oldElem, newElem) -> ctrl.onAccountSelectedAsBeneficiary(newElem));

                saleTypeDisplayer = new ChoiceBox<>();
                saleTypeDisplayer.setConverter(GUIStringTool.getSaleTypeStringConverter());
                saleTypeDisplayer.getSelectionModel()
                        .selectedItemProperty()
                        .addListener((observable, oldElem, newElem) -> ctrl.onSaleTypeSelected(newElem));

                salePriceDisplayer = new TextField();
                salePriceDisplayer.setEditable(false);
                salePriceDisplayer.setPromptText(GUIStringTool.getSalePriceTextFieldPromptText());


                GridPane mainContentBottomCenterPane = new CssEnabledGridPane();
                mainContentBottomCenterPane.add(new Label(GUIStringTool.getSaleMakerTextFieldLabel()), 0, 0);
                mainContentBottomCenterPane.add(saleMakerAccountDisplayer, 1, 0);
                mainContentBottomCenterPane.add(new Label(GUIStringTool.getSaleBeneficiaryTextFieldLabel()), 0, 1);
                mainContentBottomCenterPane.add(saleBeneficiaryAccountDisplayer, 1, 1);
                mainContentBottomCenterPane.add(new Label(GUIStringTool.getSaleTypeTextFieldLabel()), 0, 2);
                mainContentBottomCenterPane.add(saleTypeDisplayer, 1, 2);
                mainContentBottomCenterPane.add(new Label(GUIStringTool.getSalePriceTextFieldLabel()), 0, 3);
                mainContentBottomCenterPane.add(salePriceDisplayer, 1, 3);

                mainContentBottomPane.getChildren().add(mainContentBottomCenterPane);
            }
            //Sale Validator
            {
                AnchorPane anchorPane = new AnchorPane();
                validator = new SimpleValidator(true);
                validator.addListener(ctrl.getValidatorListener());
                anchorPane.getChildren().add(validator.getPane());
                AnchorPane.setRightAnchor(validator.getPane(), 0d);
                AnchorPane.setBottomAnchor(validator.getPane(), 0d);

                mainContentBottomPane.getChildren().add(anchorPane);
                HBox.setHgrow(anchorPane, Priority.ALWAYS);
            }
            mainPaneContent.setBottom(mainContentBottomPane);
        }
        /*
        ------------------------MAIN PANE------------------------
         */
        mainPane.getChildren().add(mainPaneContent);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    void switchToSaleValidationWaitingMode(long saleId, Sale sale){
        boolean clearChildren = mainPane.getChildren().size()>1;

        Pane greyIntermediaryPane = new Pane();
        greyIntermediaryPane.getStyleClass().clear();
        greyIntermediaryPane.getStyleClass().add("grey-intermediary-pane");

        saleValidationScreen = new SaleValidationScreen(saleId, sale);
        saleValidationScreen.addListener(ctrl.getSaleValidationScreenListener());
        Platform.runLater(()->{
            if(clearChildren){
                mainPane.getChildren().clear();
                mainPane.getChildren().add(mainPaneContent);
            }

            StackPane.setAlignment(saleValidationScreen.getPane(), Pos.CENTER);
            mainPane.getChildren().addAll(greyIntermediaryPane, saleValidationScreen.getPane());
        });
    }

    void switchToSaleCompositionMode(){
        Node[] childrenToRemove = mainPane.getChildren()
                                                .stream()
                                                .filter(child->!child.equals(mainPaneContent))
                                                .collect(Collectors.toList())
                                                .toArray(new Node[]{});
        Platform.runLater(()->mainPane.getChildren().removeAll(childrenToRemove));
    }

    void setProducts(List<Product> products) {
        stockDisplayer.display(products);
    }

    void setSaleTypes(List<SaleType> saleTypes) {
        Platform.runLater(()->{
            saleTypeDisplayer.getItems().clear();
            if(saleTypes!=null)
                saleTypeDisplayer.getItems().addAll(saleTypes);
        });
    }

    void setAccounts(List<Account> accounts) {
        Platform.runLater(()->{
            saleBeneficiaryAccountDisplayer.getItems().clear();
            saleBeneficiaryAccountDisplayer.getItems().add(ctrl.getDefaultAccount());
            if(accounts!=null)
                saleBeneficiaryAccountDisplayer.getItems().addAll(accounts);
        });
    }

    void setSale(Sale sale) {
        if(sale!=null) {
            saleDisplayer.display(sale);

            String price = GUIStringTool.getPriceRenderer().render(sale.getTotalPrice());
            String currentAccount = GUIStringTool.getAccountStringConverter().toString(sale.getOrderedBy());

            Platform.runLater(() -> {
                salePriceDisplayer.setText(price);
                saleMakerAccountDisplayer.setText(currentAccount);

                selectElement(saleTypeDisplayer, sale.getType());
                selectElement(saleBeneficiaryAccountDisplayer, sale.getOrderedFor());
            });
        }
    }

    private <T> void selectElement(ChoiceBox<T> choiceBox, T element){
        if(element!=null){
            if(!choiceBox.getItems().contains(element))
                choiceBox.getItems().add(element);
            choiceBox.getSelectionModel().select(element);
        }else
            choiceBox.getSelectionModel().clearSelection();
    }

    void setSaleStatus(SaleStatus status){
        this.saleValidationScreen.setSaleStatus(status);
    }

    void setValidationButtonEnabled(boolean validationButtonEnabled) {
        validator.setValidationButtonEnable(validationButtonEnabled);
    }
}
