@wip
@exotic-bets @redbook
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Exotic bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name | TEST RACE                                                                      |
      | runners   | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices    | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
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
      | Runner03 | 3 |
    And I settle race with prices
      | exotic   | <ExoticPrices>   |
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName    | BetType | BetOn                      | Stake | BalanceDeductedBy | Payout  | ExoticPrices        |
#      | Forecast Fixed | Exotic  | Runner01,Runner02          | 3.00  | 3.00              | 12.60   | 4.20 |
#      | Tricast Fixed  | Exotic  | Runner01,Runner02,Runner03 | 5.00  | 5.00              | 11.00   | 2.20 |
      | Forecast SP    | Exotic  | Runner01,Runner02          | 2.00  | 2.00              | 10.40   | 5.20 |
#      | Tricast SP     | Exotic  | Runner01,Runner02,Runner03 | 9.00  | 9.00              | 32.40   | 3.60 |