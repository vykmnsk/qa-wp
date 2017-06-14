package com.tabcorp.qa.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BetType {
    Win(1),
    Place(2),
    Eachway(3),
    Exotic(4);

    public final int id;

    BetType(int betId) {
        id = betId;
    }

    public static BetType fromName(String name) {
        for (BetType b : BetType.values()) {
            if (b.name().equalsIgnoreCase(name)) {
                return b;
            }
        }
        throw new RuntimeException(String.format("Could not find BetType with name='%s'. Available BetTypes: %s", name, allNames()));
    }

    public static List<String> allNames() {
        return Arrays.stream(BetType.values()).map(BetType::name).collect(Collectors.toList());
    }

}
