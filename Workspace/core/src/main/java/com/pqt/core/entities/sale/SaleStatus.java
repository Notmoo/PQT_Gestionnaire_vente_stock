package com.pqt.core.entities.sale;

/**
 * Created by Notmoo on 18/07/2017.
 */
public enum SaleStatus {

    PENDING(0), REFUSED(1), ACCEPTED(2), ABORTED(3);

    private int id;

    private SaleStatus(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
