package com.pqt.server.module.sale.listeners;

import com.pqt.core.entities.sale.Sale;

public interface ISaleFirerer {

    void addListener(ISaleListener l);
    void removeListener(ISaleListener l);

    void fireSaleValidatedEvent(Sale sale);
}
