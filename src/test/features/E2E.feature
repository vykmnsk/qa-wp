@e2e
Feature: End 2 End

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name | TEST RACE          |
      | runners   | Runner01, Runner02 |
      | prices    | 1.10, 2.20         |
    And I enable "<ProductName>" product settings
      | Betting | Enabled       | On    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And customer balance is at least $20.50

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<Fee>

    When I result race with the runners and positions
      | Runner01  | 1        |
      | Runner02  | 2        |
    And I settle race with prices
      | win   | <WinPrices>     |
      | place | <PlacePrices>   |
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName       | BetType | BetOn    | Stake | Fee  | Payout | WinPrices | PlacePrices |
      | Luxbook DVP Fixed | WIN     | Runner01 | 2.50  | 2.50 | 2.75   | 4.20, 4.10| 3.90,1.29   |
      | Luxbook DVP Fixed | WIN     | Runner02 | 2.50  | 2.50 | 0.00   | 2.20, 5.10| 3.90,1.29   |
      | Luxbook DVP Fixed | PLACE   | Runner01 | 2.50  | 2.50 | 2.50   | 3.20, 1.10| 3.90,1.29   |
      | Luxbook DVP Fixed | EACHWAY | Runner01 | 2.50  | 5.00 | 5.25   | 5.20, 2.10| 3.90,1.29   |
