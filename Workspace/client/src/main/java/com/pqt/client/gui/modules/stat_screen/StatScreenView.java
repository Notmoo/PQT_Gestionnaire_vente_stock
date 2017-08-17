package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.sun.deploy.util.StringUtils;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class StatScreenView implements IFXComponent {

    private TextArea statTextArea;
    private Pane mainPane;

    StatScreenView() {
        initGui();
    }

    private void initGui() {
        mainPane = new Pane();
        mainPane.getStyleClass().add(GUICssTool.getMainModulePaneCssClass());

        statTextArea = new TextArea();
        mainPane.getChildren().add(statTextArea);
        statTextArea.setId("stat-screen-text-area");
        statTextArea.setWrapText(true);
        statTextArea.setEditable(false);
        statTextArea.prefWidthProperty().bind(mainPane.widthProperty());
        statTextArea.prefHeightProperty().bind(mainPane.heightProperty());
    }

    void display(Map<String, String> statistics){
        List<String> lines = new ArrayList<>();

        if(statistics!=null)
            lines.addAll(statistics.keySet()
                .stream()
                .map(key->String.format(" * %s : %s", key, statistics.get(key)))
                .collect(Collectors.toList()));

        Platform.runLater(()->statTextArea.setText(StringUtils.join(lines, "\n")));
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }
}
