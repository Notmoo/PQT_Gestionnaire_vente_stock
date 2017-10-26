package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.startup_frame.listeners.frame.IStartupFrameModelListener;

public class StartupFrameController implements IStartupFrameModelListener {

    private final StartupFrameModel model;
    private StartupFrameView view;

    public StartupFrameController(StartupFrameModel model) {
        this.model = model;
    }

    public void setView(StartupFrameView view) {
        this.view = view;
    }

    public void updateView() {
        view.setValidationButtonEnable(enableValidationButton());
    }

    private boolean enableValidationButton() {
        return !view.getAccountUsernameTextFieldContent().isEmpty()
                && !view.getServerPortTextFieldContent().isEmpty()
                && !view.getServerPortTextFieldContent().isEmpty();
    }

    public void onValidation() {
        if(!model.isStartupProcessRunning()){
            //TODO catch following exceptions and update GUI when needed :
            //NullPointerException && IllegalArgumentException
            model.beginStartupProcess(
                    view.getServerHostTextFieldContent(),
                    view.getServerPortTextFieldContent(),
                    view.getAccountUsernameTextFieldContent(),
                    view.getAccountPasswordTextFieldContent()
            );
        }
    }

    @Override
    public void onStartupValidated() {
        view.clearPasswordField();
    }
}
