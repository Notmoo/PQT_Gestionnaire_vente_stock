package com.pqt.client.gui.modules.stock_screen.listeners;

import java.util.EventListener;

public interface IStockScreenModelListener extends EventListener {
    void onStockUpdatedEvent();
}
