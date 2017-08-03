@single-bet
Feature: Single Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Single Win Bet: <Category> | <Product> | Punter <Outcome>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 3.30, 4.40, 5.50                     |
    And I enable "<Product>" product settings
      | Betting | Enable Single | Win |
      | Betting | Display Price | Win |
    And I update fixed win prices "<WinPrices>"

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I result/settle created event race with winners "Runner01, Runner02"
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Outcome | Category     | Subcategory   | Product           | BetType | BetOn    | Stake | Deduction | Payout | WinPrices  |
      | Win     | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | Win     | Runner01 | 2.50  | 2.50      | 10.50  | 4.20, 4.10 |
      | Loss    | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | Win     | Runner02 | 3.00  | 3.00      | 0.00   | 4.20, 4.10 |

  @redbook
    Examples:
      | Outcome | Category         | Subcategory | Product | BetType | BetOn    | Stake | Deduction | Payout | WinPrices  |
      | Win     | Greyhound Racing | HARLOW      | SP      | Win     | Runner01 | 2.50  | 2.50      | 10.50  | 4.20, 4.10 |

  Scenario Outline: Single <BetType> Bet: <Category> | <Product> | Punter <Outcome>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 3.30, 4.40, 5.50                     |
    And I enable "<Product>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | Place |
      | Betting   | Enable Single | EW    |
      | Betting   | Display Price | Win   |
      | Betting   | Display Price | Place |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |
    And I update fixed win prices "<WinPrices>" and place prices "<PlacePrices>"

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I result/settle created event race with winners "Runner01, Runner02, Runner03"
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Outcome           | Category     | Subcategory   | Product           | BetType | BetOn    | Stake | Deduction | Payout | WinPrices         | PlacePrices      |
      | Win finishing 1st | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | EachWay | Runner01 | 2.50  | 5.00      | 33.13  | 10.00, 8.00, 7.00 | 3.25, 2.75, 2.25 |

    Examples:
      | Outcome           | Category     | Subcategory   | Product           | BetType | BetOn    | Stake | Deduction | Payout | WinPrices         | PlacePrices      |
      | Win finishing 2nd | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | EachWay | Runner02 | 2.25  | 4.50      | 4.01   | 8.20,  4.10, 6.30 | 2.80, 1.78, 2.33 |
      | Loss              | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | EachWay | Runner04 | 2.25  | 4.50      | 0.00   | 8.20,  4.10, 6.30 | 2.80, 1.78, 2.33 |
      | Win finishing  3d | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | EachWay | Runner03 | 4.12  | 8.24      | 6.51   | 5.20,  2.10, 3.30 | 3.90, 1.29, 1.58 |

  @redbook
    Examples:
      | Outcome           | Category         | Subcategory | Product | BetType | BetOn    | Stake | Deduction | Payout | WinPrices         | PlacePrices      |
      | Win finishing 1st | Greyhound Racing | HARLOW      | SP      | EachWay | Runner01 | 2.50  | 5.00      | 33.13  | 10.00, 8.00, 7.00 | 3.25, 2.75, 2.25 |

  @luxbet @smoke
    Examples:
      | Outcome           | Category     | Subcategory   | Product           | BetType | BetOn    | Stake | Deduction | Payout | WinPrices        | PlacePrices      |
      | Win finishing 1st | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | Place   | Runner01 | 2.50  | 2.50      | 9.75   | 3.20, 1.10, 1.00 | 3.90, 1.29, 1.00 |
