package com.pqt.core.common_resources.statistics;

public enum StatisticFields {

    TOTAL_SALE_WORTH("total_sale_worth", StatisticFieldType.SIMPLE_CURRENCY),
    TOTAL_SALE_AMOUNT("total_sale_amount", StatisticFieldType.SIMPLE_NUMBER),
    TOTAL_MONEY_MADE("total_money_made", StatisticFieldType.SIMPLE_CURRENCY),
    TOP_POPULAR_PRODUCTS("top_popular_products", StatisticFieldType.LIST),
    STAFF_SALE_WORTH("staff_sale_worth", StatisticFieldType.SIMPLE_CURRENCY),
    STAFF_SALE_AMOUNT("staff_sale_amount", StatisticFieldType.SIMPLE_NUMBER),
    GUEST_SALE_WORTH("guest_sale_worth", StatisticFieldType.SIMPLE_CURRENCY),
    GUEST_SALE_AMOUNT("guest_sale_amount", StatisticFieldType.SIMPLE_NUMBER);

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
