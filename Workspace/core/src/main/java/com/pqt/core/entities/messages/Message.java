package com.pqt.core.entities.messages;

import com.pqt.core.entities.members.PqtMember;
import com.pqt.core.entities.user_account.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

    private Map<String, String> fields;
    private MessageType type;
    private PqtMember emitter, receiver;
    private Account user;
    private Message replyTo;

    public Message(MessageType type, PqtMember emitter, PqtMember receiver, Account user, Message replyTo) {
        this(type, emitter, receiver, user, replyTo, null);
    }

    public Message(MessageType type, PqtMember emitter, PqtMember receiver, Account user, Message replyTo, Map<String, String> fields) {
        this.emitter = emitter;
        this.receiver = receiver;
        this.type = type;
        this.user = user;
        this.replyTo = replyTo;
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

    public Account getUser() {
        return user;
    }

    public Message getReplyTo() {
        return replyTo;
    }
}
