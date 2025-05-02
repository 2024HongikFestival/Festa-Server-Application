package com.hyyh.festa.domain;

public enum Prize {
    티빙구독권("티빙 구독권", 10),
    ;

    public final String prizeName;
    public final int quantity;

    Prize(String prizeName, int quantity) {
        this.prizeName = prizeName;
        this.quantity = quantity;
    }
}
