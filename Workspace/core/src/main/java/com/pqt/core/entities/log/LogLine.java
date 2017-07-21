package com.pqt.core.entities.log;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Notmoo on 18/07/2017.
 */
public class LogLine implements Serializable{
    private int id;
    private String description;
    private Date timestamp;
    private ILoggable relatedItem;
    private LogLineActionType type;

    public LogLine() {
    }

    public LogLine(int id, String description, Date timestamp, ILoggable relatedItem, LogLineActionType type) {
        this.id = id;
        this.description = description;
        this.timestamp = timestamp;
        this.relatedItem = relatedItem;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ILoggable getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(ILoggable relatedItem) {
        this.relatedItem = relatedItem;
    }

    public LogLineActionType getType() {
        return type;
    }

    public void setType(LogLineActionType type) {
        this.type = type;
    }
}
