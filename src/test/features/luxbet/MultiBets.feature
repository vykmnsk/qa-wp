@multi-bets @luxbet
Feature: Placing and Settling multi Bets

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Multi Double bets
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
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
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
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

    When I result/settle created "PAKENHAM" event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created "SEYMOUR" event race with winners "Runner11,Runner12,Runner13"
    Then customer balance is increased by $<Payout>

    Examples:
      | MultiType | BetOn             | Stake | Flexi | BalanceDeductedBy | Payout |
      | Double    | Runner01,Runner11 | 3.00  | N     | 3.00              | 165.00 |

  Scenario Outline: Horse Race Multi Treble/Doubles/Trixie/Patent bets
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
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
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
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

    When I result/settle created "BALLINA" event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created "PAKENHAM" event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created "SEYMOUR" event race with winners "Runner21,Runner22,Runner23"
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
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
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
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
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
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
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

    When I result/settle created "BALLINA" event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created "PAKENHAM" event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created "SEYMOUR" event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created "WOLVERHAMPTON" event race with winners "Runner31,Runner32,Runner33"
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
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
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
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
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
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
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
    When I enter specifics category "Horse Racing" and subcategory "CRANBOURNE"
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

    When I result/settle created "BALLINA" event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created "PAKENHAM" event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created "SEYMOUR" event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created "WOLVERHAMPTON" event race with winners "Runner31,Runner32,Runner33"
    When I result/settle created "CRANBOURNE" event race with winners "Runner41,Runner42,Runner43"
    Then customer balance is increased by $<Payout>

    Examples:
      | MultiType | BetOn                                        | Stake | Flexi | BalanceDeductedBy | Payout |
      | 5-Fold    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 92.35  |
      | Doubles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 98.58  |
      | Trebles   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 223.76 |
      | 4-Folds   | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 232.55 |
      | Canadian  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 638.24 |
      | Lucky 31  | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00  | N     | 3.00              | 654.14 |