package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.ressources.strings.IObjectStringRenderer;
import com.pqt.core.common_resources.statistics.StatisticFields;

import java.util.HashMap;
import java.util.Map;

public class StatisticFieldRenderer implements IObjectStringRenderer<StatisticFields> {

    private final Map<StatisticFields, String> strings;

    public StatisticFieldRenderer() {
        strings = new HashMap<>();
        strings.put(StatisticFields.TOTAL_MONEY_MADE, GUIStringTool.getTotalMoneyMadeStatFieldString());
        strings.put(StatisticFields.TOTAL_SALE_WORTH,GUIStringTool.getTotalSaleWorthStatFieldString());
        strings.put(StatisticFields.TOTAL_SALE_AMOUNT,GUIStringTool.getTotalSaleAmountStatFieldString());
        strings.put(StatisticFields.TOP_POPULAR_PRODUCTS,GUIStringTool.getTopPopularProductsStatFieldString());
        strings.put(StatisticFields.STAFF_SALE_AMOUNT,GUIStringTool.getStaffSaleAmountStatFieldString());
        strings.put(StatisticFields.STAFF_SALE_WORTH,GUIStringTool.getstaffSaleWorthStatFieldString());
        strings.put(StatisticFields.GUEST_SALE_AMOUNT,GUIStringTool.getGuestSaleAmountStatFieldString());
        strings.put(StatisticFields.GUEST_SALE_WORTH,GUIStringTool.getGuestSaleWorthStatFieldString());
    }

    @Override
    public String render(StatisticFields field){
        if(strings.containsKey(field))
            return strings.get(field);
        return field.getStr();
    }
}
