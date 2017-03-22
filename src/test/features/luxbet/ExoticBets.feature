@ex2e
Feature: End 2 End

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name | TEST RACE                                                                      |
      | runners   | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices    | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
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
      | ProductName    | BetType        | BetOn                               | Stake | BalanceDeductedBy | Payout | WinPrices | PlacePrices |
      | NSW Quinella   | NSW Quinella   | Runner01,Runner02                   | 3.00  | 3.00              | 1.50   |           |             |
      | NSW Exacta     | NSW Exacta     | Runner01,Runner02                   | 3.00  | 3.00              | 1.50   |           |             |
      | NSW Trifecta   | NSW Trifecta   | Runner01,Runner02,Runner03          | 3.00  | 3.00              | 1.50   |           |             |
      | NSW First Four | NSW First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | 3.00              | 1.50   |           |             |
