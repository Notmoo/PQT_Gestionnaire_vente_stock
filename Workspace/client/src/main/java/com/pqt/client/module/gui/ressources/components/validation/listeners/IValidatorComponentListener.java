package com.pqt.client.module.gui.ressources.components.validation.listeners;

import java.util.EventListener;

public interface IValidatorComponentListener extends EventListener {
    void onValidationEvent();
    void onCancelEvent();
}
