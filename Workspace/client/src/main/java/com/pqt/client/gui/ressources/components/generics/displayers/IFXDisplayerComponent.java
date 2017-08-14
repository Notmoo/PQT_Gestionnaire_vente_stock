package com.pqt.client.gui.ressources.components.generics.displayers;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.displayers.listeners.IDisplayerComponentListener;

public interface IFXDisplayerComponent<T, U extends IDisplayerComponentListener> extends IFXComponent{
    void display(T content);
    void addListener(U l);
    void removeListener(U l);
}
