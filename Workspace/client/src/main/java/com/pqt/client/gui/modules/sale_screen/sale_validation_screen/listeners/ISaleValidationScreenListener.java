package com.pqt.client.gui.modules.sale_screen.sale_validation_screen.listeners;

import java.util.EventListener;

public interface ISaleValidationScreenListener extends EventListener {
    void onScreenClose(boolean saleValidatedSuccessfully);
}
