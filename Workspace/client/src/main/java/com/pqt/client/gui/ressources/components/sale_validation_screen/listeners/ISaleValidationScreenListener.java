package com.pqt.client.gui.ressources.components.sale_validation_screen.listeners;

import java.util.EventListener;

public interface ISaleValidationScreenListener extends EventListener {
    void onScreenClose(boolean saleValidatedSuccessfully);
}
