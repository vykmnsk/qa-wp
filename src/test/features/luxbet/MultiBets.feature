@multi-bets @luxbet
Feature: Placing and Settling multi Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Racing Double bet
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Double Bet "<BetType1>"-"<BetType2>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | BetType1 | BetType2 | BetOn             | Stake | Flexi | Cost | Payout |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Win      | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 165.00 |

    Examples:
      | Category1        | Subcategory1 | Category2        | Subcategory2 | BetType1 | BetType2 | BetOn             | Stake | Flexi | Cost | Payout |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Win      | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 42.75  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Place    | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 77.55  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Place    | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 20.09  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Win      | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 165.00 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Win      | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 42.75  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Place    | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 77.55  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Place    | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 20.09  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Win      | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 165.00 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Win      | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 42.75  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Place    | Win      | Runner01,Runner11 | 3.00  | N     | 3.00 | 77.55  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Place    | Place    | Runner01,Runner11 | 3.00  | N     | 3.00 | 20.09  |

  Scenario Outline: Racing Treble bet
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Treble Bet "<BetType1>"-"<BetType2>"-"<BetType3>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | BetType1 | BetType2 | BetType3 | BetOn                      | Stake | Flexi | Cost | Payout |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Place    | Place    | Place    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 57.26  |

    Examples:
      | Category1        | Subcategory1 | Category2        | Subcategory2 | BetType1 | BetType2 | BetType3 | BetOn                      | Stake | Flexi | Cost | Payout  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Win      | Win      | Win      | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 1815.00 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Win      | Win      | Win      | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 1815.00 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Win      | Win      | Win      | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 1815.00 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Place    | Place    | Place    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 57.26   |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Place    | Place    | Place    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00 | 57.26   |
      #TODO  | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Eachway  | Eachway  | Win,Place | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00      | 165.00  |

  Scenario Outline: Racing 3-Event <MultiType> bet
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Multi Bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1        | Subcategory1 | Category2        | Subcategory2 | MultiType | BetOn                      | Stake | Flexi | Cost  | Payout  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 9.00  | 693.00  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 12.00 | 2508.00 |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 21.00 | 2589.00 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 9.00  | 693.00  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 12.00 | 2508.00 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 21.00 | 2589.00 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 9.00  | 693.00  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 12.00 | 2508.00 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 21.00 | 2589.00 |

  Scenario Outline: Racing 4-Event <MultiType> bet
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.80, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 1.50, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 1.28, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 1.78, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Multi Bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1        | Subcategory1 | Category2        | Subcategory2 | MultiType | BetOn                               | Stake | Flexi | Cost  | Payout |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00  | 18.46  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 18.00 | 45.23  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 12.00 | 47.34  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 33.00 | 111.03 |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 45.00 | 130.11 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00  | 18.46  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 18.00 | 45.23  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 12.00 | 47.34  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 33.00 | 111.03 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 45.00 | 130.11 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00  | 18.46  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 18.00 | 45.23  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 12.00 | 47.34  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 33.00 | 111.03 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 45.00 | 130.11 |

  Scenario Outline: Racing 5-Event <MultiType> bet
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.80, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 1.50, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 1.28, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 1.78, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default event with details
      | runners | Runner41, Runner42, Runner43, Runner44, Runner45, Runner46, Runner47, Runner48 |
      | prices  | 2.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"

    When I place a Luxbet Multi Bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    When I result/settle created event race with winners "Runner41,Runner42,Runner43"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1        | Subcategory1 | Category2        | Subcategory2 | MultiType | BetOn                                        | Stake | Flexi | Cost  | Payout |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00  | 36.91  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 83.39  |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 137.79 |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 15.00 | 113.16 |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 78.00 | 371.25 |
      | Horse Racing     | PAKENHAM     | Horse Racing     | SEYMOUR      | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 93.00 | 396.33 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00  | 36.91  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 83.39  |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 137.79 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 15.00 | 113.16 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 78.00 | 371.25 |
      | Greyhound Racing | CORK         | Greyhound Racing | HOBART       | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 93.00 | 396.33 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00  | 36.91  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 83.39  |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 30.00 | 137.79 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 15.00 | 113.16 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 78.00 | 371.25 |
      | Harness Racing   | ALBANY       | Harness Racing   | ALBION PARK  | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 93.00 | 396.33 |