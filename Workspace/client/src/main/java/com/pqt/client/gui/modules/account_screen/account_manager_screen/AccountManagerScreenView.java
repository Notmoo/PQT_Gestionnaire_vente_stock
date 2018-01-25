package com.pqt.client.gui.modules.account_screen.account_manager_screen;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.Collection;


class AccountManagerScreenView implements IFXComponent{

    private StackPane mainPane;
    private AccountManagerScreenController ctrl;

    private ChoiceBox<AccountLevel> levelChoiceBox;
    private TextField nameTextField;
    private PasswordField passwordField;

    AccountManagerScreenView(AccountManagerScreenController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new StackPane();

        BorderPane mainPaneContent = new BorderPane();


        GridPane mainPaneCenterContent = new GridPane();
        mainPaneCenterContent.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(GUIStringTool.getUsernameLabel());
        nameTextField = new TextField();
        nameTextField.textProperty().addListener((obs, oldVal, newVal)->ctrl.onNameChanged(oldVal, newVal));
        mainPaneCenterContent.add(nameLabel, 0, 0);
        mainPaneCenterContent.add(nameTextField, 1,0);

        Label passwordLabel = new Label(GUIStringTool.getPasswordLabel());
        passwordField = new PasswordField();
        passwordField.textProperty().addListener((obs, oldVal, newVal)->ctrl.onPasswordChanged(oldVal, newVal));
        mainPaneCenterContent.add(passwordLabel, 0, 1);
        mainPaneCenterContent.add(passwordField, 1,1);

        Label levelLabel = new Label(GUIStringTool.getUserLevelLabel());
        levelChoiceBox = new ChoiceBox<>();
        levelChoiceBox.valueProperty().addListener((obs, oldVal, newVal)->ctrl.onLevelChanged(oldVal, newVal));
        mainPaneCenterContent.add(levelLabel, 0, 2);
        mainPaneCenterContent.add(levelChoiceBox, 1,2);

        mainPaneContent.setCenter(mainPaneCenterContent);

        HBox mainPaneBottomContent = new HBox();
        mainPaneBottomContent.setFillHeight(true);
        Button validationButton = new Button(GUIStringTool.getValidationButtonLabel());
        validationButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onValidationEvent();
        });
        Button cancelButton = new Button(GUIStringTool.getCancelButtonLabel());
        cancelButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY))
                ctrl.onCancelEvent();
        });
        HBox separator = new HBox();

        mainPaneBottomContent.getChildren().addAll(separator, validationButton, cancelButton);
        HBox.setHgrow(separator, Priority.ALWAYS);

        mainPaneContent.setBottom(mainPaneBottomContent);
        mainPane.getChildren().add(mainPaneContent);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    void setLevelCollection(Collection<AccountLevel> levelCollection) {
        Platform.runLater(()->{
            levelChoiceBox.getItems().clear();
            levelChoiceBox.getItems().addAll(levelCollection);
        });
    }

    void setData(Account data) {
        Platform.runLater(()->{
            if(data!=null){
                nameTextField.setText(data.getUsername());
                passwordField.setText(data.getPassword());
                levelChoiceBox.setValue(data.getPermissionLevel());
            }else{
                nameTextField.setText("");
                passwordField.setText("");
                levelChoiceBox.setValue(levelChoiceBox.getItems()
                        .stream()
                        .min(Enum::compareTo)
                        .orElse(AccountLevel.getLowest()));
            }
        });
    }

    void lockUserNameField(boolean lock) {
        nameTextField.setDisable(lock);
    }
}
