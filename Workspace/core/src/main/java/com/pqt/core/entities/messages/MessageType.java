package com.pqt.core.entities.messages;

public enum MessageType {
    QUERY_CONNECT,
    ACK_CONNECT,
    ERR_CONNECT,
    REF_CONNECT,

    QUERY_SALE,
    ACK_SALE,
    ERR_SALE,
    REF_SALE,

    QUERY_REVERT_SALE,
    ACK_REVERT_SALE,
    ERR_REVERT_SALE,
    REF_REVERT_SALE,

    QUERY_STAT,
    MSG_STAT,
    ERR_STAT,
    REF_STAT,

    QUERY_STOCK,
    MSG_STOCK,
    ERR_STOCK,
    REF_STOCK,

    QUERY_LOGIN,
    ACK_LOGIN,
    ERR_LOGIN,
    REF_LOGIN,

    QUERY_UPDATE,
    ACK_UPDATE,
    ERR_UPDATE,
    REF_UPDATE
}
