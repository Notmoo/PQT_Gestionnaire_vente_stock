package com.pqt.client.gui.ressources.components.generics.creators;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;

public interface IFXCreatorComponent<T> extends IFXComponent{
    T create();
    boolean isCreationPossible();
}
