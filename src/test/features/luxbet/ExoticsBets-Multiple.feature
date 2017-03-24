@ex2e
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race multiple events creation
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with "<base name>", "<Runners>" and "<Prices>" details
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enabled       | On    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enable "<ProductName>" product settings
      | Betting | Enabled       | On    |
      | Betting | Enabled       | Auto  |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And customer balance is at least $20.50

    Examples:
      | base name | Runners                                                                        | Prices                                         | ProductName  |
      | TEST RACE | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80 | NSW Quinella |
#      | TEST RACE | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80 | NSW Exacta   |
#      | TEST RACE | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80 | NSW Trifecta |
#      | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80 | NSW First Four |

#  Scenario Outline: Horse Race Exotic Bets
#    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake>
#    Then customer balance is decreased by $<BalanceDeductedBy>
#
#    When I result race with the runners and positions
#      | Runner01 | 1 |
#      | Runner02 | 2 |
#    And I settle race with prices
#    Then customer balance is increased by $<Payout>
#
#    Examples:
#      | BetType  | BetOn                      | Stake | BalanceDeductedBy | Payout |
#      | Quinella | Runner01,Runner02          | 3.00  | 3.00              | 1.50   |
#      | Exacta   | Runner01,Runner02          | 3.00  | 6.00              | 1.50   |
#      | Trifecta | Runner01,Runner02,Runner03 | 3.00  | 18.00             | 1.50   |