package com.pqt.client.module.gui.ressources.components.sale;

import com.pqt.client.module.gui.ressources.components.IFXComponent;
import com.pqt.client.module.gui.ressources.components.sale.listeners.ISaleComponentListener;
import com.pqt.core.entities.sale.Sale;

public interface IFXSaleDisplayerComponent extends IFXComponent {
    void display(Sale sale);
    void addListener(ISaleComponentListener l);
    void removeListener(ISaleComponentListener l);
}
