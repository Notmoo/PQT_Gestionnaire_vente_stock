package com.pqt.core.common_resources.statistics;

public enum StatisticFields {

    TOTAL_SALE_WORTH("total_sale_worth", StatisticFieldType.SIMPLE),
    TOTAL_SALE_AMOUNT("total_sale_amount", StatisticFieldType.SIMPLE),
    TOTAL_MONEY_MADE("total_money_made", StatisticFieldType.SIMPLE),
    TOP_POPULAR_PRODUCTS("top_popular_products", StatisticFieldType.LIST),
    STAFF_SALE_WORTH("staff_sale_worth", StatisticFieldType.SIMPLE),
    STAFF_SALE_AMOUNT("staff_sale_amount", StatisticFieldType.SIMPLE),
    GUEST_SALE_WORTH("guest_sale_worth", StatisticFieldType.SIMPLE),
    GUEST_SALE_AMOUNT("guest_sale_amount", StatisticFieldType.SIMPLE);

    private final String str;
    private final StatisticFieldType type;

    StatisticFields(String str, StatisticFieldType type) {
        this.str = str;
        this.type = type;
    }

    public String getStr() {
        return str;
    }

    public StatisticFieldType getType() {
        return type;
    }
}
