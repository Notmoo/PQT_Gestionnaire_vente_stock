package com.pqt.client.gui.frames.startup_frame;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class StartupFrameView implements IFXComponent{

    private VBox mainPane;
    private final StartupFrameController ctrl;

    private TextField serverHostTextField;
    private TextField serverPortTextField;
    private TextField usernameTextField;
    private TextField passwordTextField;

    private Button validationButton;
    private Text infoText;

    public StartupFrameView(StartupFrameController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        try {
            mainPane = new VBox();

            //TODO to uncomment
            /*
            Label serverHostLabel = new Label(GUIStringTool.getServerHostLabel());
            serverHostTextField = makeTextField(TextField.class);
            Label serverPortLabel = new Label(GUIStringTool.getServerPortLabel());
            serverPortTextField = makeTextField(TextField.class);
            /**/

            //TODO to remove
            Label serverHostLabel = new Label(GUIStringTool.getServerHostLabel());
            serverHostTextField = new TextField("localhost");
            serverHostTextField.textProperty().addListener((obs, oldVal, newVal)->ctrl.updateView());
            Label serverPortLabel = new Label(GUIStringTool.getServerPortLabel());
            serverPortTextField = new TextField("8080");
            serverPortTextField.textProperty().addListener((obs, oldVal, newVal)->ctrl.updateView());
            /**/

            GridPane serverFieldGridPane = new GridPane();
            serverFieldGridPane.add(serverHostLabel, 0, 0);
            serverFieldGridPane.add(serverHostTextField, 1, 0);
            serverFieldGridPane.add(serverPortLabel, 0, 1);
            serverFieldGridPane.add(serverPortTextField, 1, 1);

            TitledPane serverTitledPane = new TitledPane(GUIStringTool.getServerSectionTitleLabel(), serverFieldGridPane);


            Label usernameLabel = new Label(GUIStringTool.getUsernameLabel());
            usernameTextField = makeTextField(TextField.class);
            Label passwordLabel = new Label(GUIStringTool.getPasswordLabel());
            passwordTextField = makeTextField(PasswordField.class);

            GridPane accountFieldGridPane = new GridPane();
            accountFieldGridPane.add(usernameLabel, 0, 0);
            accountFieldGridPane.add(usernameTextField, 1, 0);
            accountFieldGridPane.add(passwordLabel, 0, 1);
            accountFieldGridPane.add(passwordTextField, 1, 1);

            TitledPane accountTitledPane = new TitledPane(GUIStringTool.getAccountSectionTitleLabel(), accountFieldGridPane);

            validationButton = new Button(GUIStringTool.getValidationButtonLabel());
            validationButton.setOnAction((event) -> {
                ctrl.onValidation();
            });

            infoText = new Text("");
            infoText.getStyleClass().add("text-displayer");
            TitledPane errorConsoleTitledPane = new TitledPane(GUIStringTool.getErrorConsoleSectionTitleLabel(), infoText);
            infoText.textProperty().addListener((obs, oldValue, newValue)->errorConsoleTitledPane.setExpanded(true));

            mainPane.getChildren().addAll(serverTitledPane, accountTitledPane, errorConsoleTitledPane, validationButton);
        }catch(Exception e){
            //TODO Shutdown software on exception
            e.printStackTrace();
        }
    }

    private <T extends TextField> T makeTextField(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T ntf = clazz.newInstance();
        ntf.textProperty().addListener((obs, oldVal, newVal)->ctrl.updateView());
        return ntf;
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

    public void setValidationButtonEnable(boolean enable) {
        this.validationButton.setDisable(!enable);
    }

    public void clearPasswordField() {
        passwordTextField.setText("");
    }

    public void displayError(String errorMsg) {
        infoText.setText(errorMsg);
    }

    public void clearErrorField() {
        infoText.setText("");
    }
}
