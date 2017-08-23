package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.modules.stat_screen.listeners.IStatScreenModelListener;
import com.pqt.client.module.stat.StatService;
import com.pqt.client.module.stat.listeners.IStatListener;
import com.pqt.client.module.stat.listeners.StatListenerAdapter;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.Map;

class StatScreenModel {
    private EventListenerList listenerList;
    private StatService statService;

    StatScreenModel(StatService statService) {
        this.statService = statService;
        listenerList = new EventListenerList();
        this.statService.addListener(new StatListenerAdapter() {
            @Override
            public void onGetStatSuccess() {
                Arrays.stream(listenerList.getListeners(IStatScreenModelListener.class)).forEach(IStatScreenModelListener::onStatisticsChangedEvent);
            }
        });
    }

    Map<String, String> getStatistics() {
        return statService.getStats();
    }

    void addListener(IStatScreenModelListener l){
        listenerList.add(IStatScreenModelListener.class, l);
    }

    void removeListener(IStatScreenModelListener l){
        listenerList.remove(IStatScreenModelListener.class, l);
    }
}
