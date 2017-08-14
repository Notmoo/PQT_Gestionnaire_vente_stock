package com.pqt.client.gui.modules.sale_screen.sale_validation_screen;

import com.pqt.client.gui.modules.sale_screen.sale_validation_screen.listeners.ISaleValidationScreenListener;
import com.pqt.client.gui.ressources.components.generics.javafx_override.CssEnabledGridPane;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.sale.Sale;
import com.pqt.core.entities.sale.SaleStatus;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

public class SaleValidationScreen {

    private Pane mainPane;

    private TextField saleStatusTextField;
    private SaleStatus saleStatus;
    private ProgressIndicator progressIndicator;
    private Button validationButton;

    private EventListenerList listeners;

    public SaleValidationScreen(long saleId, Sale sale) {
        listeners = new EventListenerList();
        mainPane = new Pane();

        saleStatus = sale.getStatus();

        BorderPane mainPaneContent = new BorderPane();

        GridPane centerPane = new CssEnabledGridPane();
        centerPane.setAlignment(Pos.CENTER);

        Label saleIdLabel = new Label(GUIStringTool.getSaleIdLabel());
        centerPane.add(saleIdLabel, 0, 0);

        TextField saleIdTextField = new TextField(Long.toString(saleId));
        saleIdTextField.setEditable(false);
        centerPane.add(saleIdTextField, 1, 0);

        Label saleStatusLabel = new Label(GUIStringTool.getSaleStatusLabel());
        centerPane.add(saleStatusLabel, 0, 1);

        saleStatusTextField = new TextField(GUIStringTool.getSaleStatusRenderer().render(saleStatus));
        saleStatusTextField.setEditable(false);
        centerPane.add(saleStatusTextField, 1, 1);

        validationButton = new Button(GUIStringTool.getOkButtonLabel());
        validationButton.setDisable(saleStatus.equals(SaleStatus.PENDING));
        validationButton.setOnMouseClicked(event->fireScreenClose(saleStatus.equals(SaleStatus.ACCEPTED)));
        centerPane.add(validationButton, 1,2);

        mainPaneContent.setCenter(centerPane);

        progressIndicator = new ProgressIndicator();
        mainPaneContent.setLeft(progressIndicator);

        mainPaneContent.prefHeightProperty().bind(mainPane.heightProperty());
        mainPaneContent.prefWidthProperty().bind(mainPane.widthProperty());
        mainPane.getChildren().add(mainPaneContent);
    }

    private void fireScreenClose(boolean saleValidatedSuccessFully) {
        if(!validationButton.isDisable()){
            Arrays.stream(listeners.getListeners(ISaleValidationScreenListener.class))
                    .forEach(listener->listener.onScreenClose(saleValidatedSuccessFully));
        }
    }

    public void addListener(ISaleValidationScreenListener listener){
        listeners.add(ISaleValidationScreenListener.class, listener);
    }

    public void setSaleStatus(SaleStatus status){
        saleStatus = status;
        Platform.runLater(()->{
            validationButton.setDisable(saleStatus.equals(SaleStatus.PENDING));
            saleStatusTextField.setText(GUIStringTool.getSaleStatusRenderer().render(status));
            progressIndicator.setProgress((status.equals(SaleStatus.PENDING)?ProgressIndicator.INDETERMINATE_PROGRESS:1F));
        });
    }

    public Pane getPane(){
        return mainPane;
    }
}
