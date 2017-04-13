@multi-bets @luxbet
Feature: Placing and Settling multi Bets

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Multi Double bets
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    Then customer balance is increased by $<Payout>

    Examples:
      | MultiType | BetOn             | Stake | Flexi | BalanceDeductedBy | Payout |
      | Double    | Runner01,Runner11 | 3.00  | N     | 3.00              | 7.59   |

  Scenario Outline: Horse Race Multi Treble/Doubles/Trixie/Patent bets
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is increased by $<Payout>

    Examples:
      | MultiType | BetOn                      | Stake | Flexi | BalanceDeductedBy | Payout |
      | Treble    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 17.46  |
      | Doubles   | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 25.05  |
      | Trixie    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 39.51  |
      | Patent    | Runner01,Runner11,Runner21 | 3.00  | N     | 3.00              | 47.61  |

  Scenario Outline: Horse Race Multi 4-Fold/Doubles/Trebles/Yankee/Lucky15 bets
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And customer balance is at least $20.50

    When I place a multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    Then customer balance is increased by $<Payout>

    Examples:
      | MultiType | BetOn                               | Stake | Flexi | BalanceDeductedBy | Payout |
      | 4-Fold    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 40.15  |
      | Doubles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 55.38  |
      | Trebles   | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 79.88  |
      | Yankee    | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 169.41 |
      | Lucky 15  | Runner01,Runner11,Runner21,Runner31 | 3.00  | N     | 3.00              | 181.41 |

  Scenario Outline: Horse Race Multi 5-Fold/Doubles/Trebles/4-Folds/Canadian/Lucky31 bets
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner41, Runner42, Runner43, Runner44, Runner45, Runner46, Runner47, Runner48 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
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
      | MultiType | BetOn                                        | Stake | Flexi | BalanceDeductedBy | Payout |
      | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 92.35  |
      | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 98.58  |
      | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 223.76 |
      | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 232.55 |
      | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 638.24 |
      | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 654.14 |