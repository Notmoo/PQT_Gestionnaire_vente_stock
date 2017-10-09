package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class StartupFrameView implements IFXComponent{

    private VBox mainPane;
    private final StartupFrameController ctrl;

    private TextField serverHostTextField;
    private TextField serverPortTextField;
    private TextField usernameTextField;
    private TextField passwordTextField;

    public StartupFrameView(StartupFrameController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new VBox();

        Label serverHostLabel = new Label(GUIStringTool.getServerHostLabel());
        serverHostTextField = new TextField();
        Label serverPortLabel = new Label(GUIStringTool.getServerPortLabel());
        serverPortTextField = new TextField();

        GridPane serverFieldGridPane = new GridPane();
        serverFieldGridPane.add(serverHostLabel,0,0);
        serverFieldGridPane.add(serverHostTextField,1,0);
        serverFieldGridPane.add(serverPortLabel,0,1);
        serverFieldGridPane.add(serverPortTextField,1,1);

        TitledPane serverTitledPane = new TitledPane(GUIStringTool.getServerSectionTitleLabel(),serverFieldGridPane);


        Label usernameLabel = new Label(GUIStringTool.getUsernameLabel());
        usernameTextField = new TextField();
        Label passwordLabel = new Label(GUIStringTool.getPasswordLabel());
        passwordTextField = new PasswordField();

        GridPane accountFieldGridPane = new GridPane();
        accountFieldGridPane.add(usernameLabel,0,0);
        accountFieldGridPane.add(usernameTextField,1,0);
        accountFieldGridPane.add(passwordLabel,0,1);
        accountFieldGridPane.add(passwordTextField,1,1);

        TitledPane accountTitledPane = new TitledPane(GUIStringTool.getAccountSectionTitleLabel(),accountFieldGridPane);

        Button validationButton = new Button(GUIStringTool.getValidationButtonLabel());
        validationButton.setOnAction((event)->{
            ctrl.onValidation();
        });

        mainPane.getChildren().addAll(serverTitledPane, accountTitledPane, validationButton);
    }

    String getServerHostTextFieldContent(){
        return serverHostTextField.getText();
    }

    String getServerPortTextFieldContent(){
        return serverPortTextField.getText();
    }

    String getAccountUsernameTextFieldContent(){
        return usernameTextField.getText();
    }

    String getAccountPasswordTextFieldContent(){
        return passwordTextField.getText();
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }
}
