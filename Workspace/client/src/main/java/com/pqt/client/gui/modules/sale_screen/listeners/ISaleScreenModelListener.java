package com.pqt.client.gui.modules.sale_screen.listeners;

import com.pqt.core.entities.sale.SaleStatus;

import java.util.EventListener;

public interface ISaleScreenModelListener extends EventListener {
    void onSaleValidatedEvent();
    void onSaleNotValidatedEvent(SaleStatus status, Throwable cause);
    void onStockUpdatedEvent();
    void onAccountConnectedStateUpdatedEvent();
    void onAccountListUpdatedEvent();
}
