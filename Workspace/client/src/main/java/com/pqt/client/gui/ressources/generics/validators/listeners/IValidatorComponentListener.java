package com.pqt.client.gui.ressources.generics.validators.listeners;

import java.util.EventListener;

public interface IValidatorComponentListener extends EventListener {
    void onValidationEvent();
    void onCancelEvent();
}
