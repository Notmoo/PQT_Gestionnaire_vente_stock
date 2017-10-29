package com.pqt.server.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

import java.util.EventListener;

public interface ISaleListener extends EventListener{
    void onSaleValidatedEvent(Sale sale);
}
