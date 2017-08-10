package com.pqt.client.module.gui.ressources.components.validation;

import com.pqt.client.module.gui.ressources.components.IFXComponent;
import com.pqt.client.module.gui.ressources.components.products.listeners.IStockComponentListener;
import com.pqt.client.module.gui.ressources.components.validation.listeners.IValidatorComponentListener;

public interface IFXValidatorComponent extends IFXComponent{
    void addListener(IValidatorComponentListener l);
    void removeListener(IValidatorComponentListener l);
}
