@multi-bets @luxbet
Feature: Placing and Settling multi Bets

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Racing Multi Double bets
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
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
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    Then customer balance is increased by $<Payout>

    Examples:
      | CategoryOne      | SubcategoryOne | CategoryTwo      | SubcategoryTwo | MultiType          | BetOn             | Stake | Flexi | BalanceDeductedBy | Payout |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Double,Win-Win     | Runner01,Runner11 | 3.00  | N     | 3.00              | 165.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Double,Win-Place   | Runner01,Runner11 | 3.00  | N     | 3.00              | 42.75  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Double,Place-Win   | Runner01,Runner11 | 3.00  | N     | 3.00              | 77.55  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Double,Place-Place | Runner01,Runner11 | 3.00  | N     | 3.00              | 20.09  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Double,Win-Win     | Runner01,Runner11 | 3.00  | N     | 3.00              | 165.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Double,Win-Place   | Runner01,Runner11 | 3.00  | N     | 3.00              | 42.75  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Double,Place-Win   | Runner01,Runner11 | 3.00  | N     | 3.00              | 77.55  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Double,Place-Place | Runner01,Runner11 | 3.00  | N     | 3.00              | 20.09  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Double,Win-Win     | Runner01,Runner11 | 3.00  | N     | 3.00              | 165.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Double,Win-Place   | Runner01,Runner11 | 3.00  | N     | 3.00              | 42.75  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Double,Place-Win   | Runner01,Runner11 | 3.00  | N     | 3.00              | 77.55  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Double,Place-Place | Runner01,Runner11 | 3.00  | N     | 3.00              | 20.09  |

  Scenario Outline: Racing Multi Treble/Doubles/Trixie/Patent bets
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
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
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
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
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is increased by $<Payout>

    Examples:
      | CategoryOne      | SubcategoryOne | CategoryTwo      | SubcategoryTwo | MultiType | BetOn                      | Stake | Flexi | BalanceDeductedBy | Payout  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Treble    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 1815.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 687.00  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2499.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2571.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Treble    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 1815.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 687.00  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2499.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2571.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Treble    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 1815.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 687.00  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2499.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 2571.00 |

  Scenario Outline: Racing Multi 4-Fold/Doubles/Trebles/Yankee/Lucky15 bets
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
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
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
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
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    Then customer balance is increased by $<Payout>

    Examples:
      | CategoryOne      | SubcategoryOne | CategoryTwo      | SubcategoryTwo | MultiType | BetOn                               | Stake | Flexi | BalanceDeductedBy | Payout   |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 9075.00  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 1083.00  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 5271.00  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15423.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15507.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 9075.00  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 1083.00  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 5271.00  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15423.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15507.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 9075.00  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 1083.00  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 5271.00  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15423.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 15507.00 |

  Scenario Outline: Racing Multi 5-Fold/Doubles/Trebles/4-Folds/Canadian/Lucky31 bets
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
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
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
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
    When I enter specifics category "<CategoryTwo>" and subcategory "<SubcategoryTwo>"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
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
    When I enter specifics category "<CategoryOne>" and subcategory "<SubcategoryOne>"
    And I create a default event with details
      | runners | Runner41, Runner42, Runner43, Runner44, Runner45, Runner46, Runner47, Runner48 |
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
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    When I result/settle created event race with winners "Runner41,Runner42,Runner43"
    Then customer balance is increased by $<Payout>

    Examples:
      | CategoryOne      | SubcategoryOne | CategoryTwo      | SubcategoryTwo | MultiType | BetOn                                        | Stake | Flexi | BalanceDeductedBy | Payout   |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 30000.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 1551.00  |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 10743.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 35463.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77748.00 |
      | Horse Racing     | PAKENHAM       | Horse Racing     | SEYMOUR        | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77844.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 30000.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 1551.00  |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 10743.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 35463.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77748.00 |
      | GREYHOUND RACING | CORK           | GREYHOUND RACING | HOBART         | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77844.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 30000.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 1551.00  |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 10743.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 35463.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77748.00 |
      | Harness Racing   | ALBANY         | Harness Racing   | ALBION PARK    | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 77844.00 |