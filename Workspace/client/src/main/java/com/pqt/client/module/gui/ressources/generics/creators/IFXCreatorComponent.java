package com.pqt.client.module.gui.ressources.generics.creators;

import com.pqt.client.module.gui.ressources.generics.IFXComponent;

public interface IFXCreatorComponent<T> extends IFXComponent{
    T create();
    boolean isCreationPossible();
}
