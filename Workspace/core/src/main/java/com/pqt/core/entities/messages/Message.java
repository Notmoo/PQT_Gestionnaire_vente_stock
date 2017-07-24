package com.pqt.core.entities.messages;

import com.pqt.core.entities.members.PqtMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

    private Map<String, String> fields;
    private MessageType type;
    private PqtMember emitter, receiver;

    public Message(MessageType type, PqtMember emitter, PqtMember receiver) {
        this(type, emitter, receiver, null);
    }

    public Message(MessageType type, PqtMember emitter, PqtMember receiver, Map<String, String> fields) {
        this.emitter = emitter;
        this.receiver = receiver;
        this.type = type;
        this.fields = new HashMap<>();
        if(fields!=null)
            for(String key : fields.keySet()){
                this.fields.put(key, fields.get(key));
            }
    }

    public Map<String, String> getFields() {
        return new HashMap<>(fields);
    }

    public boolean hasField(String header){
        return fields.containsKey(header);
    }

    public String getField(String header){
        return fields.get(header);
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
