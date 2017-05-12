@multi-bets @redbook
Feature: Placing and Settling multi Bets

  Background:
    Given Existing customer with at least $20.00 balance is logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Horse Race Multi bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi | Win |
      | Betting | Enable Multi | EW  |
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi | Win |
      | Betting | Enable Multi | EW  |

    When I place "<BetType>" multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    When I result race with the runners and positions
      | Runner11 | 1 |
      | Runner12 | 2 |
      | Runner13 | 3 |
    And I settle race

    Then customer balance is increased by $<Payout>

    Examples:
      | BetType | MultiType | BetOn             | Stake | BalanceDeductedBy | Payout |
      | Win     | Double    | Runner01,Runner11 | 3.00  | 3.00              | 7.59   |
      | Eachway | Double    | Runner01,Runner11 | 3.00  | 6.00              | 10.59  |