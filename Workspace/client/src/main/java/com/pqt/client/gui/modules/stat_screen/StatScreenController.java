package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.modules.stat_screen.listeners.IStatScreenModelListener;

class StatScreenController implements IStatScreenModelListener{

    private StatScreenModel model;
    private StatScreenView view;

    StatScreenController(StatScreenModel model) {
        this.model = model;
    }

    void setView(StatScreenView view) {
        this.view = view;
    }

    @Override
    public void onStatisticsChangedEvent() {
        view.display(model.getStatistics());
    }
}
