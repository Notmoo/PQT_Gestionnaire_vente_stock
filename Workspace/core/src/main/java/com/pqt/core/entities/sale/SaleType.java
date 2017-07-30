package com.pqt.core.entities.sale;

/**
 * Created by Notmoo on 18/07/2017.
 */
public enum SaleType {
    CASH(1), BANK_CHECK(1), STUDENT_ASSOCIATION_ACCOUNT(1), OFFERED_GUEST(0), OFFERED_STAFF_MEMBER(0);

    private double priceMultiplier;

    SaleType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
