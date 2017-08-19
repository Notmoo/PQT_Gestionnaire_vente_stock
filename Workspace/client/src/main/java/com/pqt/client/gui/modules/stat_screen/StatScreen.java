package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.module.stat.StatService;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.scene.layout.Pane;

public class StatScreen implements IGuiModule {

    private StatScreenView view;

    public StatScreen(StatService statService) {
        StatScreenModel model = new StatScreenModel(statService);
        StatScreenController ctrl = new StatScreenController(model);
        view = new StatScreenView();

        ctrl.setView(view);
    }

    @Override
    public String getModuleName() {
        return GUIStringTool.getStatGuiModuleName();
    }

    @Override
    public AccountLevel getLowestRequiredAccountLevel() {
        return AccountLevel.STAFF;
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
