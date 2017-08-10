package com.pqt.client.module.gui.ressources.generics.displayers;

import com.pqt.client.module.gui.ressources.generics.IFXComponent;
import com.pqt.client.module.gui.ressources.generics.displayers.listeners.IDisplayerComponentListener;

public interface IFXDisplayerComponent<T, U extends IDisplayerComponentListener> extends IFXComponent{
    void display(T content);
    void addListener(U l);
    void removeListener(U l);
}
