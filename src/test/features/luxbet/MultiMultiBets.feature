@multi-multi-bets @luxbet
Feature: Placing and Settling multi-multi Bets

  Background:
    Given A new default customer with $500.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page
    And I update the customer AML status to "Manually Verified"

  Scenario Outline: Racing Multi-Multi <MultiType> bets
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
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
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.50, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
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
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
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

    When I place a Luxbet Multi-Treble "<MultiType>" bet on types "<BetType1>"-"<BetType2>"-"<BetType3>" on the runners "<BetOn>" for $"<Stake>" with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | MultiType                    | BetType1 | BetType2 | BetType3 | BetOn                      | Stake               | Flexi   | Cost  | Payout |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Doubles,Trixie,Patent | Win      | Win      | Win      | Runner01,Runner11,Runner21 | 3.00,3.00,3.00,3.00 | N,N,N,N | 45.00 | 191.25 |

    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | MultiType                    | BetType1 | BetType2 | BetType3 | BetOn                      | Stake               | Flexi   | Cost  | Payout |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Doubles,Trixie,Patent | Place    | Place    | Place    | Runner01,Runner11,Runner21 | 3.00,3.00,3.00,3.00 | N,N,N,N | 45.00 | 389.58 |
      #TODO | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR  | Treble,Doubles,Trixie,Patent | Eachway | Eachway | Eachway | Runner01,Runner11,Runner21 | 3.00,3.00,3.00,3.00 | N,N,N,N | 3.00   | 3026.52 |

  Scenario Outline: Racing Multi-Multi <MultiType> bets
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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

    When I place a Luxbet Multi-Multi bet on "<MultiType>" on the runners "<BetOn>" for $"<Stake>" with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | MultiType                    | BetOn                      | Stake               | Flexi   | Cost  | Payout  |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Trixie                | Runner01,Runner11,Runner21 | 3.00,3.00           | Y,Y     | 6.00  | 2442.00 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Trixie                | Runner01,Runner11,Runner21 | 3.00,3.00           | N,N     | 15.00 | 4323.00 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Trixie                | Runner01,Runner11,Runner21 | 3.00,3.00           | Y,Y     | 6.00  | 2442.00 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Treble,Doubles,Trixie,Patent | Runner01,Runner11,Runner21 | 3.00,3.00,3.00,3.00 | Y,Y,Y,Y | 11.94 | 3035.46 |

  Scenario Outline: Racing Multi-Multi <MultiType> bets
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 3.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
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
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.50, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
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
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 3.50, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                                |
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
    When I enter specifics category "<Category2>" and subcategory "<Subcategory2>"
    And I create a default Racing event with details
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

    When I place a Luxbet Multi-Multi bet on "<MultiType>" on the runners "<BetOn>" for $"<Stake>" with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | MultiType                       | BetOn                               | Stake               | Flexi   | Cost   | Payout |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | 4-Fold,Doubles,Trebles          | Runner01,Runner11,Runner21,Runner31 | 3.00,3.00,3.00      | N,N,N   | 33.00  | 159.00 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Doubles,Trebles,Yankee,Lucky 15 | Runner01,Runner11,Runner21,Runner31 | 3.00,3.00,3.00,3.00 | N,N,N,N | 108.00 | 504.00 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | 4-Fold,Doubles,Trebles          | Runner01,Runner11,Runner21,Runner31 | 3.00,3.00,3.00      | Y,Y,Y   | 9.00   | 33.07  |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Doubles,Trebles,Yankee,Lucky 15 | Runner01,Runner11,Runner21,Runner31 | 3.00,3.00,3.00,3.00 | Y,Y,Y,Y | 11.97  | 59.79  |

  Scenario Outline: Racing Multi <MultiType> bets
    When I enter specifics category "<Category1>" and subcategory "<Subcategory1>"
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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
    And I create a default Racing event with details
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

    When I place a Luxbet Multi-Multi bet on "<MultiType>" on the runners "<BetOn>" for $"<Stake>" with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    When I result/settle created event race with winners "Runner31,Runner32,Runner33"
    When I result/settle created event race with winners "Runner41,Runner42,Runner43"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category1    | Subcategory1 | Category2    | Subcategory2 | MultiType                                 | BetOn                                        | Stake                    | Flexi     | Cost   | Payout  |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | 5-Fold,Doubles,Trebles                    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00,3.00,3.00           | N,N,N     | 63.00  | 258.09  |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Doubles,Trebles,4-Folds,Canadian,Lucky 31 | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00,3.00,3.00,3.00,3.00 | N,N,N,N,N | 246.00 | 1101.92 |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | 5-Fold,Doubles,Trebles                    | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00,3.00,3.00           | Y,Y,Y     | 9.00   | 59.03   |
      | Horse Racing | PAKENHAM     | Horse Racing | SEYMOUR      | Doubles,Trebles,4-Folds,Canadian,Lucky 31 | Runner01,Runner11,Runner21,Runner31,Runner41 | 3.00,3.00,3.00,3.00,3.00 | Y,Y,Y,Y,Y | 14.65  | 70.26   |