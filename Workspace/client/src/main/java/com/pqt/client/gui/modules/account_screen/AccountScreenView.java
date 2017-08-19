package com.pqt.client.gui.modules.account_screen;

import com.pqt.client.gui.modules.account_screen.account_manager_screen.AccountManagerScreen;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class AccountScreenView implements IFXComponent{

    private AccountScreenController ctrl;
    private StackPane mainPane;
    private BorderPane mainPaneContent;
    private TableView<Account> tableView;
    private AccountManagerScreen accountManagerScreen;

    private Button addAccountButton, detailAccountButton, removeAccountButton;


    AccountScreenView(AccountScreenController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new StackPane();
        mainPane.getStyleClass().add(GUICssTool.getMainModulePaneCssClass());

        mainPaneContent = new BorderPane();

        tableView = new TableView<>();

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)-> ctrl.onSelectedAccountChanged(oldVal, newVal));
        TableColumn<Account, String> nameColumn = new TableColumn<>(GUIStringTool.getAccountNameColumnHeaderLabel());
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUsername()));
        tableView.getColumns().add(nameColumn);

        TableColumn<Account, String> levelColumn = new TableColumn<>(GUIStringTool.getAccountLevelColumnHeaderLabel());
        levelColumn.setCellValueFactory(param -> new SimpleStringProperty(
                            GUIStringTool.getAccountLevelStringRenderer().render(param.getValue().getPermissionLevel())
        ));
        tableView.getColumns().add(levelColumn);
        mainPaneContent.setCenter(tableView);

        HBox mainPaneTopContent = new HBox();
        mainPaneTopContent.setFillHeight(true);

        Label label = new Label(GUIStringTool.getAccountListTitleLabel());
        label.getStyleClass().add(GUICssTool.getTitleTextStyleClass());
        mainPaneTopContent.getChildren().add(label);

        HBox separator = new HBox();
        ToolBar buttonToolbar = new ToolBar();
        addAccountButton = new Button(GUIStringTool.getAddButtonLabel());
        addAccountButton.setOnMouseClicked(event->{
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onAddAccountRequested();
        });
        buttonToolbar.getItems().add(addAccountButton);
        detailAccountButton = new Button(GUIStringTool.getDetailButtonLabel());
        detailAccountButton.setOnMouseClicked(event->{
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onDetailAccountRequested();
        });
        buttonToolbar.getItems().add(detailAccountButton);
        removeAccountButton = new Button(GUIStringTool.getRemoveButtonLabel());
        removeAccountButton.setOnMouseClicked(event->{
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onRemoveAccountRequested();
        });
        buttonToolbar.getItems().add(removeAccountButton);
        Button refreshAccountButton = new Button(GUIStringTool.getRefreshButtonLabel());
        refreshAccountButton.setOnMouseClicked(event->{
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onRefreshAccountRequested();
        });
        buttonToolbar.getItems().add(refreshAccountButton);

        mainPaneTopContent.getChildren().addAll(separator, buttonToolbar);
        HBox.setHgrow(separator, Priority.ALWAYS);

        mainPaneContent.setTop(mainPaneTopContent);
        mainPane.getChildren().add(mainPaneContent);
    }

    boolean isItemSelected(){
        return tableView.getSelectionModel().getSelectedItem()!=null;
    }

    Account getSelectedItem(){
        return tableView.getSelectionModel().getSelectedItem();
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    boolean isDetailCreationPossible() {
        return accountManagerScreen!=null && accountManagerScreen.isCreationPossible();
    }

    Account getDetailScreenInitialValue() {
        if(accountManagerScreen!=null)
            return accountManagerScreen.getInitialValue();
        return null;
    }

    Account getDetailScreenCreatedValue() {
        if(accountManagerScreen!=null)
            return accountManagerScreen.create();
        return null;
    }

    boolean isDetailScreenShown(){
        return accountManagerScreen!=null;
    }

    void showDetailScreen(Account initialValue, Collection<AccountLevel> availableLevels){
        if(!isDetailScreenShown()){
            Pane separator = new Pane();
            separator.getStyleClass().add(GUICssTool.getIntermediaryPaneStyleClass());
            accountManagerScreen = new AccountManagerScreen(initialValue, availableLevels);
            accountManagerScreen.addListener(ctrl.getDetailScreenValidationListener());

            Platform.runLater(()->{
                mainPane.getChildren().addAll(separator, accountManagerScreen.getPane());
            });
        }
    }

    void hideDetailScreen(){
        if(isDetailScreenShown()){
            List<Node> toRemove = mainPane.getChildren()
                    .stream()
                    .filter(node->!node.equals(mainPaneContent))
                    .collect(Collectors.toList());

            Platform.runLater(()->{
                mainPane.getChildren().removeAll(toRemove);
                accountManagerScreen = null;
            });

        }
    }

    void setAccountCollection(Collection<Account> accountCollection) {
        Platform.runLater(()->{
            tableView.getItems().clear();
            if(accountCollection!=null)
                tableView.getItems().addAll(accountCollection);
        });
    }

    void setAddAccountActionLocked(boolean locked) {
        addAccountButton.setDisable(locked);
    }

    void setDetailAccountActionLocked(boolean locked) {
        detailAccountButton.setDisable(locked);
    }

    void setRemoveAccountActionLocked(boolean locked) {
        removeAccountButton.setDisable(locked);
    }
}
