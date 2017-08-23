package com.pqt.client.gui.ressources.components.generics.others.listeners;

import java.util.EventListener;

public interface ISideBarListener extends EventListener {

    void onCollapsedFinished();
    void onExpandFinished();
}
