package com.pqt.client.module.gui.ressources.generics.validators.listeners;

public interface IValidatorComponentFirerer {
    void addListener(IValidatorComponentListener l);
    void removeListener(IValidatorComponentListener l);

    void fireValidationEvent();
    void fireCancelEvent();
}
