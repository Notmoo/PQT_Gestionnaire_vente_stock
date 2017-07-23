package com.pqt.core.entities.messages;

import com.pqt.core.entities.members.PqtMember;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private List<Field> fields;
    private MessageType type;
    private PqtMember emitter, receiver;

    public Message(MessageType type, PqtMember emitter, PqtMember receiver) {
        this(type, emitter, receiver, null);
    }

    public Message(MessageType type, PqtMember emitter, PqtMember receiver, List<Field> fields) {
        this.emitter = emitter;
        this.receiver = receiver;
        this.type = type;
        this.fields = new ArrayList<>();
        if(fields!=null)
            this.fields.addAll(fields);
    }

    public List<Field> getFields() {
        return new ArrayList<>(fields);
    }

    public PqtMember getEmitter() {
        return emitter;
    }

    public PqtMember getReceiver() {
        return receiver;
    }

    public MessageType getType() {
        return type;
    }
}
