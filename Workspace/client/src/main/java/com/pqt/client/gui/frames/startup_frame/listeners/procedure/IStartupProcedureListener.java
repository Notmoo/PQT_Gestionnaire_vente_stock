package com.pqt.client.gui.frames.startup_frame.listeners.procedure;

import java.util.EventListener;

public interface IStartupProcedureListener extends EventListener{

    void onServerFoundEvent(String URL, Integer Port);
    void onUserAccountUnknownEvent(String username);
    void onUserAccountConnectedEvent(String username);
    void onUserAccountDisconnectedEvent(String username);
}
