package com.pqt.client.gui.ressources.components.generics.javafx_override;

import javafx.css.PseudoClass;
import javafx.scene.control.ListCell;

public class HighlightListCell<T> extends ListCell<T> {

    private PseudoClass highlightPC;
    private boolean highlighted;

    public HighlightListCell() {
        super();
        highlighted = false;
        highlightPC = PseudoClass.getPseudoClass("highlighted");
    }

    public void setHighLight(boolean highLight){
        highlighted = highLight;
        this.pseudoClassStateChanged(highlightPC, highLight);
    }

    public boolean isHightlighted(){
        return highlighted;
    }
}
