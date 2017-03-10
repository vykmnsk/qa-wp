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

    When I settle race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName       | BetType | BetOn    | Stake | Fee  | Payout |
      | Luxbook DVP Fixed | Win     | Runner01 | 2.50  | 2.50 | 2.75   |
      | Luxbook DVP Fixed | Win     | Runner02 | 2.50  | 2.50 | 0.00   |
      | Luxbook DVP Fixed | Place   | Runner01 | 2.50  | 2.50 | 2.50   |
      | Luxbook DVP Fixed | EachWay | Runner01 | 2.50  | 5.00 | 5.25   |
