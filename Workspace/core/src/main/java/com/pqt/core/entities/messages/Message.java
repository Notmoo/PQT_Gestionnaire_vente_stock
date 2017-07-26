package com.pqt.core.entities.messages;

import com.pqt.core.entities.members.PqtMember;

import java.util.*;

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

    @Override
    public int hashCode() {
        return Objects.hash(fields, emitter, receiver, type);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!this.getClass().isInstance(obj))
            return false;

        Message other = Message.class.cast(obj);
        return Objects.equals(this.fields, other.fields)
                && Objects.equals(this.emitter, other.emitter)
                && Objects.equals(this.receiver, other.receiver)
                && Objects.equals(this.type, other.type);
    }
}
