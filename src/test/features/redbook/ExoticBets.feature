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
    Then customer balance after bet is decreased by $<Deduction>

    When I result event with winners "<BetOn>"
    And I settle race with Exotic prices "<ExoticPrices>"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | ProductName | BetType | BetOn                        | Stake | Deduction | Payout | ExoticPrices |
      | Forecast SP | Exotic  | Runner01, Runner02           | 2.00  | 2.00      | 10.40  | 5.20         |
      | Tricast SP  | Exotic  | Runner01, Runner02, Runner03 | 9.00  | 9.00      | 32.40  | 3.60         |


  @exotic-sp-boxed
  Scenario Outline: Horse Race Exotic Boxed <ProductName> bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "<ProductName>" product settings
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake> with boxed as "<Boxed>"

    Then customer balance after bet is decreased by $<Deduction>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |

    And I settle race with Exotic prices "<ExoticPrices>"
    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | ProductName | BetType | BetOn                               | Stake | Deduction | Payout | ExoticPrices | Boxed |
      | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 2.00      | 10.40  | 5.20         | N     |
      | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 4.00      | 10.40  | 5.20         | Y     |
      | Forecast SP | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Forecast SP | Exotic  | Runner01,Runner02,Runner04          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 2.00      | 7.20   | 3.60         | N     |
      | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 7.20   | 3.60         | Y     |
      | Tricast SP  | Exotic  | Runner01,Runner02,Runner03,Runner04 | 2.00  | 48.00     | 7.20   | 3.60         | Y     |
