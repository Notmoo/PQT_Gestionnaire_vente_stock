package com.pqt.core.entities.messages;

public enum MessageType {
    ERROR_QUERY,
    REFUSED_QUERY,

    QUERY_SALE,
    ACK_SALE,

    QUERY_REVERT_SALE,
    ACK_REVERT_SALE,

    QUERY_LAST_SALES_LIST,
    MSG_LAST_SALES_LIST,

    QUERY_STAT,
    MSG_STAT,

    QUERY_STOCK,
    MSG_STOCK,

    QUERY_ACCOUNT_LIST,
    MSG_ACCOUNT_LIST,

    QUERY_CONNECT_ACCOUNT,
    ACK_CONNECT_ACCOUNT,

    QUERY_STOCK_UPDATE,
    ACK_STOCK_UPDATE,

    QUERY_ACCOUNT_UPDATE,
    ACK_ACCOUNT_UPDATE,

    QUERY_PING,
    ACK_PING,

    QUERY_CONFIG_LIST,
    MSG_CONFIG_LIST,

    // no fields, permission at least Waiter
    QUERY_SIMPLIFIED_PRODUCT_LIST,
    ACK_SIMPLIFIED_PRODUCT_LIST,

    // no fields, permission at least Waiter
    QUERY_SERVING_LIST,
    ACK_SERVING_LIST,

    // fields (sale), permission at least Waiter
    QUERY_SERVING_DONE,
    ACK_SERVING_DONE,

    // no fields, permission at least Waiter
    QUERY_SERVING_VERSION,
    ACK_SERVING_VERSION

}
