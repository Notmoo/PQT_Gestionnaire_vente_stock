package com.pqt.client.gui.ressources.components.generics.javafx_override;

import javafx.scene.layout.GridPane;

public class CssEnabledGridPane extends GridPane {

    public CssEnabledGridPane() {
        super();
        this.getStyleClass().add("grid-pane");
    }
}
