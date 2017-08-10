package com.pqt.client.module.gui.ressources.components.sale;

import com.pqt.client.module.gui.ressources.components.IFXComponent;
import com.pqt.core.entities.sale.Sale;

public interface IFXSaleCreatorComponent extends IFXComponent{
    Sale create();
}
