package com.pqt.core.entities.messages;

public enum MessageType {
    ERROR_QUERY,
    REFUSED_QUERY,

    QUERY_CONNECT,
    ACK_CONNECT,

    QUERY_SALE,
    ACK_SALE,

    QUERY_REVERT_SALE,
    ACK_REVERT_SALE,

    QUERY_STAT,
    MSG_STAT,

    QUERY_STOCK,
    MSG_STOCK,

    QUERY_LOGIN,
    ACK_LOGIN,

    QUERY_UPDATE,
    ACK_UPDATE
}
