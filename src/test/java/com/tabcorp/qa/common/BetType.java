package com.tabcorp.qa.common;

public enum BetType {
    Win(1),
    Place(2),
    Eachway(3),
    Exotic(4);

    public final int id;

    BetType(int betId) {
        id = betId;
    }

}
