package com.pqt.client.gui.ressources.components;

import com.pqt.client.gui.ressources.components.generics.validators.IFXValidatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentFirerer;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.SimpleValidatorComponentFirerer;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SimpleValidator implements IFXValidatorComponent {

    private final IValidatorComponentFirerer firerer;
    private Pane pane;
    private boolean askConfirmation;
    private Button validationButton, cancelButton;

    public SimpleValidator() {
        this(false);
    }

    public SimpleValidator(boolean askConfirmation) {
        firerer = new SimpleValidatorComponentFirerer();
        this.askConfirmation = askConfirmation;
    }

    @Override
    public void addListener(IValidatorComponentListener l) {
        firerer.addListener(l);
    }

    @Override
    public void removeListener(IValidatorComponentListener l) {
        firerer.removeListener(l);
    }

    @Override
    public Pane getPane() {
        if(pane == null)
            pane = createPane();
        return pane;
    }

    private Pane createPane(){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("validator");

        validationButton = new Button(GUIStringTool.getValidationButtonLabel());
        validationButton.setOnMouseClicked(event->{
            getValidationButtonProcess().process();
        });
        grid.add(validationButton, 0,0);

        cancelButton = new Button(GUIStringTool.getCancelButtonLabel());
        cancelButton.setOnMouseClicked(event->{
            getCancelButtonProcess().process();
        });
        grid.add(cancelButton, 1, 0);

        return grid;
    }

    private IButtonProcess getValidationButtonProcess(){
        return ()->{
            if(validationButton.getText().equals(GUIStringTool.getValidationButtonLabel())){
                if(askConfirmation)
                    Platform.runLater(()->validationButton.setText(GUIStringTool.getConfirmationValidationButtonLabel()));
                else
                    firerer.fireValidationEvent();
            }else{
                if(validationButton.getText().equals(GUIStringTool.getConfirmationValidationButtonLabel()))
                    firerer.fireValidationEvent();
                Platform.runLater(()->validationButton.setText(GUIStringTool.getValidationButtonLabel()));
            }
        };
    }

    private IButtonProcess getCancelButtonProcess(){
        return ()->{
            if(cancelButton.getText().equals(GUIStringTool.getCancelButtonLabel())){
                if(askConfirmation)
                    Platform.runLater(()->cancelButton.setText(GUIStringTool.getConfirmationCancelButtonLabel()));
                else
                    firerer.fireCancelEvent();
            }else{
                Platform.runLater(()->cancelButton.setText(GUIStringTool.getCancelButtonLabel()));
                firerer.fireCancelEvent();
            }
        };


    }

    private interface IButtonProcess{
        void process();
    }

    public void setValidationButtonEnable(boolean enable){
        this.validationButton.setDisable(!enable);
    }

    public void setCancelationButtonEnable(boolean enable){
        this.cancelButton.setDisable(!enable);
    }
}
