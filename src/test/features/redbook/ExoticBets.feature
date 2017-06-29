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

  @smoke
    Examples:
      | ProductName | BetType | BetOn              | Stake | Deduction | Payout | ExoticPrices |
      | Forecast SP | Exotic  | Runner01, Runner02 | 2.00  | 2.00      | 10.40  | 5.20         |

    Examples:
      | ProductName | BetType | BetOn                        | Stake | Deduction | Payout | ExoticPrices |
      | Tricast SP  | Exotic  | Runner01, Runner02, Runner03 | 9.00  | 9.00      | 32.40  | 3.60         |


  @exotic-sp-boxed
  Scenario Outline: <Category> Exotic <ProductName> Bet with boxed type field set to <Boxed>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
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
      | Category         | Subcategory   | ProductName | BetType | BetOn                               | Stake | Deduction | Payout | ExoticPrices | Boxed |
      | Horse Racing     | WOLVERHAMPTON | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 2.00      | 10.40  | 5.20         | N     |
      | Horse Racing     | WOLVERHAMPTON | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 4.00      | 10.40  | 5.20         | Y     |
      | Horse Racing     | WOLVERHAMPTON | Forecast SP | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Horse Racing     | WOLVERHAMPTON | Forecast SP | Exotic  | Runner01,Runner02,Runner04          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Horse Racing     | WOLVERHAMPTON | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 2.00      | 7.20   | 3.60         | N     |
      | Horse Racing     | WOLVERHAMPTON | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 7.20   | 3.60         | Y     |
      | Horse Racing     | WOLVERHAMPTON | Tricast SP  | Exotic  | Runner01,Runner02,Runner03,Runner04 | 2.00  | 48.00     | 7.20   | 3.60         | Y     |
      | Greyhound Racing | BELLE VUE     | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 2.00      | 10.40  | 5.20         | N     |
      | Greyhound Racing | BELLE VUE     | Forecast SP | Exotic  | Runner01,Runner02                   | 2.00  | 4.00      | 10.40  | 5.20         | Y     |
      | Greyhound Racing | BELLE VUE     | Forecast SP | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Greyhound Racing | BELLE VUE     | Forecast SP | Exotic  | Runner01,Runner02,Runner04          | 2.00  | 12.00     | 10.40  | 5.20         | Y     |
      | Greyhound Racing | BELLE VUE     | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 2.00      | 7.20   | 3.60         | N     |
      | Greyhound Racing | BELLE VUE     | Tricast SP  | Exotic  | Runner01,Runner02,Runner03          | 2.00  | 12.00     | 7.20   | 3.60         | Y     |

  @smoke
    Examples:
      | Category         | Subcategory | ProductName | BetType | BetOn                               | Stake | Deduction | Payout | ExoticPrices | Boxed |
      | Greyhound Racing | BELLE VUE   | Tricast SP  | Exotic  | Runner01,Runner02,Runner03,Runner04 | 2.00  | 48.00     | 7.20   | 3.60         | Y     |