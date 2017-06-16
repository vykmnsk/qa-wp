@multi-bets @redbook
Feature: Placing and Settling multi Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Horse Race Double
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
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
    And I create a default event with details
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
      | BetType | NumOfPlaces | BetOn              | Stake | Deduction | Payout |
      | Win     | 3           | Runner01, Runner11 | 3.00  | 3.00      | 7.59   |
      | Eachway | 3           | Runner01, Runner11 | 3.00  | 6.00      | 10.67  |


  Scenario Outline: Horse Race <MultiType>
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
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
    And I create a default event with details
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
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
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
      | MultiType | BetType | NumOfPlaces | BetOn                        | Stake | Deduction | Payout |
      | Treble    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 8.00      | 105.25 |
      | Trixie    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 32.00     | 266.93 |
      | Patent    | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 56.00     | 335.73 |
      | Doubles   | Win     | 3           | Runner01, Runner11, Runner21 | 8.00  | 24.00     | 161.68 |

