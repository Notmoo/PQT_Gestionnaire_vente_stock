package com.pqt.core.entities.user_account;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Notmoo on 18/07/2017.
 */
public enum AccountLevel {
    LOWEST, GUEST, STAFF, WAITER, MASTER;

    public static AccountLevel getLowest(){
        return Arrays.stream(AccountLevel.values()).sorted(Comparator.naturalOrder()).findFirst().orElse(null);
    }

    public static AccountLevel getHighest(){
        return Arrays.stream(AccountLevel.values()).sorted(Comparator.reverseOrder()).findFirst().orElse(null);
    }
}
