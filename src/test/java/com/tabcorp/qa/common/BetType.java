package com.tabcorp.qa.common;

public enum BetType {
    Win(1),
    Place(2),
    EachWay(3);

    public final int id;

    BetType(int betId) {
        id = betId;
    }

}
