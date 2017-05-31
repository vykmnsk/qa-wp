@exotic-bets @redbook
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Horse Race Exotic bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<BalanceDeductedBy>

    When I result event with winners "<BetOn>"
    And I settle race with Exotic prices "<ExoticPrices>"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | ProductName    | BetType | BetOn                        | Stake | BalanceDeductedBy | Payout | ExoticPrices |
#      | Forecast Fixed | Exotic  | Runner01, Runner02           | 3.00  | 3.00              | 12.60  | 4.20         |
#      | Tricast Fixed  | Exotic  | Runner01, Runner02, Runner03 | 5.00  | 5.00              | 11.00  | 2.20         |
      | Forecast SP    | Exotic  | Runner01, Runner02           | 2.00  | 2.00              | 10.40  | 5.20         |
      | Tricast SP     | Exotic  | Runner01, Runner02, Runner03 | 9.00  | 9.00              | 32.40  | 3.60         |