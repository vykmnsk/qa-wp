@redbook @placespaid

Feature: Placing and Settling several Single bets on an Event with Number of Places on Bet Enabled

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  @placespaid-before
  Scenario Outline: <ProductName> Bet Placement before Scratching for <Category> Event with Number of Places on Bet Enabled
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I edit the category options settings
      | Number of Places on Bet | Y |
      | Recalculate Place Price | N |
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |
    When I place a single "Win" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner02" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner03" for $<Stake>
    And I place a single "Win" bet on the runner "Runner08" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I scratch the runners at position(s) "8"
    And I update market details
      | Market Status      | Live             |
      | E/W                | yes              |
      | Bets Allowed       | WIN Yes          |
      | Bets Allowed Place | PLACE Fraction   |
      | Place Fraction     | 1/4              |
      | No of Places       | <NewNumOfPlaces> |
    Then I can see the following deduction details on settlement page
      | 8 Runner08 | 30 | 30 |

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category         | Subcategory | ProductName       | NumOfPlaces | NewNumOfPlaces | Stake | Deduction | TotalPayout |
      | Horse Racing     | LIMERICK    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 16.00     | 12.81       |
      | Greyhound Racing | BALLARAT    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 16.00     | 12.81       |

  @placespaid-after
  Scenario Outline: <ProductName> Bet Placement after Scratching for <Category> Event with Number of Places on Bet Enabled
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I edit the category options settings
      | Number of Places on Bet | Y |
      | Recalculate Place Price | N |
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |

    When I scratch the runners at position(s) "8"
    And I update market details
      | Place Fraction     | 1/4              |
      | No of Places       | <NewNumOfPlaces> |
    Then I can see the following deduction details on settlement page
      | 8 Runner08 | 30 | 30 |

    When I place a single "Win" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner02" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner03" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category         | Subcategory | ProductName       | NumOfPlaces | NewNumOfPlaces | Stake | Deduction | TotalPayout |
      | Horse Racing     | LIMERICK    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 14.00     | 9.05        |
      | Greyhound Racing | BALLARAT    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 14.00     | 9.05        |

  @placespaid-both
  Scenario Outline: <ProductName> Bet Placement before and after Scratching for <Category> Event with Number of Places on Bet Enabled
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I edit the category options settings
      | Number of Places on Bet | Y |
      | Recalculate Place Price | N |
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | <NumOfPlaces>  |

    When I place a single "Win" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner02" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner03" for $<Stake>
    And I place a single "Win" bet on the runner "Runner08" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction1>

    When I scratch the runners at position(s) "8"
    And I update market details
      | Market Status      | Live             |
      | E/W                | yes              |
      | Bets Allowed       | WIN Yes          |
      | Bets Allowed Place | PLACE Fraction   |
      | Place Fraction     | 1/4              |
      | No of Places       | <NewNumOfPlaces> |
    Then I can see the following deduction details on settlement page
      | 8 Runner08 | 30 | 30 |

    When I place a single "Win" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner01" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner02" for $<Stake>
    And I place a single "Eachway" bet on the runner "Runner03" for $<Stake>
    Then customer balance after bet is decreased by $<Deduction2>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category         | Subcategory | ProductName       | NumOfPlaces | NewNumOfPlaces | Stake | Deduction1 | Deduction2 | TotalPayout |
      | Horse Racing     | LIMERICK    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 16.00      | 28.00      | 19.86       |
      | Greyhound Racing | BALLARAT    | Luxbook DVP Fixed | 3           | 2              | 2.00  | 16.00      | 28.00      | 19.86       |