package com.tabcorp.qa.common;

/**
 * Created by suniaram on 10/3/17.
 */
public enum BetType {
    WIN(1),
    PLACE(2),
    EACHWAY(3);

    public final int id;

    BetType(int betId) {
        id = betId;
    }

    public String getName() {
        return name().toLowerCase();
    }

}
