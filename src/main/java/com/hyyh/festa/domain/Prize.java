package com.hyyh.festa.domain;

public enum Prize {
    A("에어팟", 5),
    B("변신로봇", 3),
    C("장검", 7),
    ;

    public final String prizeName;
    public final int quantity;

    Prize(String prizeName, int quantity) {
        this.prizeName = prizeName;
        this.quantity = quantity;
    }
}
