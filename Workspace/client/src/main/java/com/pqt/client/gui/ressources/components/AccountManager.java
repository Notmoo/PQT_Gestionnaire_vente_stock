package com.pqt.client.gui.ressources.components;

import com.pqt.client.gui.ressources.components.generics.creators.IFXCreatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.IFXValidatorComponent;
import com.pqt.client.gui.ressources.components.specifics.account.listeners.IAccountComponentListener;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.SimpleValidatorComponentFirerer;
import com.pqt.client.gui.ressources.components.specifics.account.IFXAccountsDisplayerComponent;
import com.pqt.client.gui.ressources.components.specifics.account.listeners.SimpleAccountComponentFirerer;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.user_account.Account;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class AccountManager implements IFXAccountsDisplayerComponent, IFXValidatorComponent, IFXCreatorComponent<Account> {

    private Pane mainPane;

    private HBox mainDisconnectedPane, mainConnectedPane;
    private TextField connectedUsernameField;
    private ChoiceBox<Account> disconnectedUsernameField;
    private PasswordField passwordField;

    private SimpleAccountComponentFirerer accountEventFirerer;
    private SimpleValidatorComponentFirerer validatorEventFirerer;

    private Account currentAccount;

    public AccountManager() {
        accountEventFirerer = new SimpleAccountComponentFirerer();
        validatorEventFirerer = new SimpleValidatorComponentFirerer();

        currentAccount = null;

        init();
    }

    private void init() {
        mainPane = new Pane();

        mainConnectedPane = new HBox();
        mainDisconnectedPane = new HBox();

        connectedUsernameField = new TextField();
        connectedUsernameField.setEditable(false);

        Button disconnectButton = new Button(GUIStringTool.getLogoutButtonLabel());
        disconnectButton.setOnMouseClicked(event->validatorEventFirerer.fireCancelEvent());
        disconnectButton.setOnKeyTyped(event->{if(event.getCode().equals(KeyCode.ENTER)) validatorEventFirerer.fireCancelEvent();});

        mainConnectedPane.getChildren().addAll(connectedUsernameField, disconnectButton);


        disconnectedUsernameField = new ChoiceBox<>();
        disconnectedUsernameField.setConverter(GUIStringTool.getAccountStringConverter());

        passwordField = new PasswordField();
        passwordField.setPromptText(GUIStringTool.getPasswordFieldPromptText());

        VBox leftDisconnectedPaneContent = new VBox();
        leftDisconnectedPaneContent.getChildren().addAll(disconnectedUsernameField, passwordField);

        Button validationButton = new Button(GUIStringTool.getLoginButtonLabel());
        validationButton.setOnMouseClicked(event-> validatorEventFirerer.fireValidationEvent());
        validationButton.setOnKeyTyped(event->{if(event.getCode().equals(KeyCode.ENTER)) validatorEventFirerer.fireValidationEvent();});

        mainDisconnectedPane.getChildren().addAll(leftDisconnectedPaneContent, validationButton);

        refreshMainPane();
    }

    @Override
    public void display(Collection<Account> content) {
        Platform.runLater(()->disconnectedUsernameField.setItems(FXCollections.observableArrayList(content)));
    }

    public void setCurrentAccount(Account account){
        currentAccount = account;
        Platform.runLater(()->connectedUsernameField.setText(GUIStringTool.getAccountStringConverter().toString(currentAccount)));
        refreshMainPane();
    }

    private void refreshMainPane() {
        if(currentAccount!=null)
            Platform.runLater(
                    ()->{
                        mainPane.getChildren().clear();
                        mainPane.getChildren().add(mainConnectedPane);
                    }
            );
        else
            Platform.runLater(
                    ()->{
                        mainPane.getChildren().clear();
                        mainPane.getChildren().add(mainDisconnectedPane);
                    }
            );
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    @Override
    public void addListener(IAccountComponentListener l) {
        accountEventFirerer.addListener(l);
    }

    @Override
    public void removeListener(IAccountComponentListener l) {
        accountEventFirerer.removeListener(l);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    @Override
    public void addListener(IValidatorComponentListener l) {
        validatorEventFirerer.addListener(l);
    }

    @Override
    public void removeListener(IValidatorComponentListener l) {
        validatorEventFirerer.removeListener(l);
    }

    @Override
    public Account create() {
        if(!isCreationPossible())
            return null;

        return new Account(disconnectedUsernameField.getValue().getUsername(), passwordField.getText(), disconnectedUsernameField.getValue().getPermissionLevel());
    }

    @Override
    public boolean isCreationPossible() {
        return currentAccount==null
                && !disconnectedUsernameField.getAccessibleText().isEmpty()
                && !passwordField.getText().isEmpty();
    }
}
