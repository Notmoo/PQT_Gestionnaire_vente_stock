package com.pqt.client.gui.ressources.components.generics.javafx_override;

import javafx.scene.control.ListCell;

public class HighlightListCell<T> extends ListCell<T> {
    public void setHighLight(boolean highLight){
        if(highLight){
            if(!getStyleClass().contains("list-cell-highlighted"))
                getStyleClass().add("list-cell-highlighted");
        }
        else
            getStyleClass().remove("list-cell-highlighted");
    }

    public boolean isHightlighted(){
        return getStyleClass().contains("list-cell-highlighted");
    }
}
