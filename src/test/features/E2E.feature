@e2e
Feature: End 2 End

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name | TEST RACE                              |
      | runners   | Runner01, Runner02, Runner03, Runner04 |
      | prices    | 1.10, 2.20, 1.20, 2.40                 |
    And I enable "<ProductName>" product settings
      | Betting | Enabled       | On    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enabled       | On    |
      | Betting | Enabled       | Auto  |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And customer balance is at least $20.50

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
    And I settle race with prices
      | Win   | <WinPrices>   |
      | Place | <PlacePrices> |
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName       | BetType        | BetOn                               | Stake | BalanceDeductedBy | Payout | WinPrices  | PlacePrices |
      | Luxbook DVP Fixed | Win            | Runner01                            | 2.50  | 2.50              | 2.75   | 4.20, 4.10 | 3.90,1.29   |
      | Luxbook DVP Fixed | Win            | Runner02                            | 2.50  | 2.50              | 0.00   | 2.20, 5.10 | 3.90,1.29   |
      | Luxbook DVP Fixed | Place          | Runner01                            | 2.50  | 2.50              | 2.50   | 3.20, 1.10 | 3.90,1.29   |
      | Luxbook DVP Fixed | EachWay        | Runner01                            | 2.50  | 5.00              | 5.25   | 5.20, 2.10 | 3.90,1.29   |
      | NSW Quinella      | NSW Quinella   | Runner01,Runner02                   | 3.00  |                   |        |            |             |
      | NSW Exacta        | NSW Exacta     | Runner01,Runner02                   | 3.00  |                   |        |            |             |
      | NSW Trifecta      | NSW Trifecta   | Runner01,Runner02,Runner03          | 3.00  |                   |        |            |             |
      | NSW First Four    | NSW First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  |                   |        |            |             |
