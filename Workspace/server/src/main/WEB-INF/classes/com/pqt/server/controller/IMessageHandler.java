package com.pqt.server.controller;

import com.pqt.core.entities.messages.Message;

//TODO écrire Javadoc
public interface IMessageHandler {
    Message handleMessage(Message message);
}
