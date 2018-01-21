package com.pqt.client.gui.frames.startup_frame.listeners.frame;

public interface IStartupFrameModelEventFirerer {
    void fireStartupValidated();
    void fireStartupFailed();

    void addListener(IStartupFrameModelListener l);
    void removeListener(IStartupFrameModelListener l);
}
