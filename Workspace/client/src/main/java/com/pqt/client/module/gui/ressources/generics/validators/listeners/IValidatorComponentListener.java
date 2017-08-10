package com.pqt.client.module.gui.ressources.generics.validators.listeners;

import java.util.EventListener;

public interface IValidatorComponentListener extends EventListener {
    void onValidationEvent();
    void onCancelEvent();
}
