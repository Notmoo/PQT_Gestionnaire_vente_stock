package com.pqt.client.gui.frames.startup_frame;

import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.frames.startup_frame.listeners.frame.IStartupFrameModelListener;

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
            try {
                model.beginStartupProcess(
                        view.getServerHostTextFieldContent(),
                        view.getServerPortTextFieldContent(),
                        view.getAccountUsernameTextFieldContent(),
                        view.getAccountPasswordTextFieldContent()
                );
            }catch(Exception e){
                view.displayError(GUIStringTool.getExceptionFormatter().render(e));
            }
        }
    }

    @Override
    public void onStartupValidated() {
        view.clearErrorField();
        view.clearPasswordField();
    }

    @Override
    public void onStartupFailed() {
        view.clearErrorField();
        view.displayError("Echec de la connexion");
        view.clearPasswordField();
    }
}
