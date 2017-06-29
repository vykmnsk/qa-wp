package com.tabcorp.qa.common;

import com.tabcorp.qa.wagerplayer.api.WAPI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public enum BetType {
    Win(1),
    Place(2),
    Eachway(3),
    Exotic(4);

    public final int id;

    BetType(int betId) {
        id = betId;
    }

    public static BetType fromName(String nameInput) {
        final String searchName = nameInput.replace("/", "").replace("WinPlace", "Eachway");
        BetType found = Arrays.stream(BetType.values())
                .filter(bt -> bt.name().equalsIgnoreCase(searchName))
                .findFirst().orElse(null);
        assertThat(found).withFailMessage(String.format(
                "Could not find BetType with name='%s'. Available BetTypes: %s",
                searchName, allNames()))
                .isNotNull();
        return found;
    }

    public static List<String> allNames() {
        return Arrays.stream(BetType.values()).map(BetType::name).collect(Collectors.toList());
    }

}
