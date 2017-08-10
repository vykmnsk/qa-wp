@multi-bets @redbook
Feature: Placing and Settling multi Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Multi bet with 2 events <Category>  <Multitype>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

    When I place a Redbook Multi Bet "Double" "<BetType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I result/settle created event race with winners "Runner01, Runner02, Runner03"
    When I result/settle created event race with winners "Runner11, Runner12, Runner13"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category     | Subcategory   | BetType | NumOfPlaces | BetOn              | Stake | Deduction | Payout |
      | Horse Racing | WOLVERHAMPTON | Win     | 3           | Runner01, Runner11 | 3.00  | 3.00      | 7.59   |

    Examples:
      | Category         | Subcategory | BetType | NumOfPlaces | BetOn              | Stake | Deduction | Payout |
      | Greyhound Racing | HARLOW      | Eachway | 3           | Runner01, Runner11 | 3.00  | 6.00      | 10.67  |


  Scenario Outline: Multi bet with 3 events <Category>  <Multitype>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 5.20, 2.20, 8.20, 10.40, 12.00, 2.60, 4.40, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |

    When I place a Redbook Multi Bet "<MultiType>" "<BetType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I result/settle created event race with winners "Runner01, Runner02, Runner03"
    When I result/settle created event race with winners "Runner11, Runner12, Runner13"
    When I result/settle created event race with winners "Runner21, Runner22, Runner23"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category         | Subcategory   | MultiType | BetType | NumOfPlaces | BetOn                        | Stake | Deduction | Payout |
      | Horse Racing     | WOLVERHAMPTON | Treble    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 8.00      | 105.25 |
      | Horse Racing     | WOLVERHAMPTON | Trixie    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 32.00     | 266.93 |
      | Horse Racing     | WOLVERHAMPTON | Patent    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 56.00     | 335.73 |
      | Greyhound Racing | HARLOW        | Treble    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 8.00      | 105.25 |
      | Greyhound Racing | HARLOW        | Trixie    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 32.00     | 266.93 |

  @smoke
    Examples:
      | Category         | Subcategory | MultiType | BetType | NumOfPlaces | BetOn                        | Stake | Deduction | Payout |
      | Greyhound Racing | HARLOW      | Patent    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 56.00     | 335.73 |


  Scenario Outline: Multi bet with 4 events <Category>  <Multitype>

    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 5.20, 2.20, 8.20, 10.40, 12.00, 2.60, 4.40, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 2.20, 4.20, 8.00, 2.40, 6.30, 10.60, 12.00, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |

    When I place a Redbook Multi Bet "<MultiType>" "<BetType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>
    When I result/settle created event race with winners "Runner01, Runner02, Runner03"
    When I result/settle created event race with winners "Runner11, Runner12, Runner13"
    When I result/settle created event race with winners "Runner21, Runner22, Runner23"
    When I result/settle created event race with winners "Runner31, Runner32, Runner33"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category         | Subcategory   | MultiType | BetType | BetOn                                  | Stake | Deduction | Payout | NumOfPlaces |
      | Horse Racing     | WOLVERHAMPTON | 4-Fold    | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 2.00      | 57.89  | 3           |
      | Horse Racing     | WOLVERHAMPTON | Yankee    | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 22.00     | 251.38 | 3           |
      | Horse Racing     | WOLVERHAMPTON | Lucky 15  | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 30.00     | 272.98 | 3           |
      | Greyhound Racing | HARLOW        | 4-Fold    | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 2.00      | 57.89  | 3           |
      | Greyhound Racing | HARLOW        | Yankee    | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 22.00     | 251.38 | 3           |
      | Greyhound Racing | HARLOW        | Lucky 15  | Win     | Runner01, Runner11, Runner21, Runner31 | 2.00  | 30.00     | 272.98 | 3           |


  Scenario Outline: Multi bet with 5 events <Category>  <Multitype>

    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 5.20, 2.20, 8.20, 10.40, 12.00, 2.60, 4.40, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner31, Runner32, Runner33, Runner34, Runner35, Runner36, Runner37, Runner38 |
      | prices  | 2.20, 4.20, 8.00, 2.40, 6.30, 10.60, 12.00, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner41, Runner42, Runner43, Runner44, Runner45, Runner46, Runner47, Runner48 |
      | prices  | 2.00, 4.20, 8.00, 2.40, 6.30, 10.60, 12.00, 2.80                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |

    When I place a Redbook Multi Bet "<MultiType>" "<BetType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>
    When I result/settle created event race with winners "Runner01, Runner02, Runner03"
    When I result/settle created event race with winners "Runner11, Runner12, Runner13"
    When I result/settle created event race with winners "Runner21, Runner22, Runner23"
    When I result/settle created event race with winners "Runner31, Runner32, Runner33"
    When I result/settle created event race with winners "Runner41, Runner42, Runner43"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | Category         | Subcategory   | MultiType | BetType | BetOn                                            | Stake | Deduction | Payout | NumOfPlaces |
      | Horse Racing     | WOLVERHAMPTON | Canadian  | Win     | Runner01, Runner11, Runner21, Runner31, Runner41 | 2.00  | 52.00     | 797.34 | 3           |
      | Greyhound Racing | HARLOW        | Canadian  | Win     | Runner01, Runner11, Runner21, Runner31, Runner41 | 2.00  | 52.00     | 797.34 | 3           |