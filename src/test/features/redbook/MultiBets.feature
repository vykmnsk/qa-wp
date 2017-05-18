@multi-bets @redbook
Feature: Placing and Settling multi Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Horse Race Multi bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 2.30, 4.30, 1.50, 3.46, 3.30, 2.90, 4.40, 5.85                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

    When I place "<BetType>" multi bet "<MultiType>" on the runners "<BetOn>" for $<Stake>
    Then customer balance after bet is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions "<WinnerWithPositionsEvent1>"
    And I settle race
    When I result race with the runners and positions "<WinnerWithPositionsEvent2>"
    And I settle race

    Then customer balance since last bet is increased by $<Payout>

    Examples:
      | BetType | MultiType | BetOn             | Stake | BalanceDeductedBy | Payout | WinnerWithPositionsEvent1          | WinnerWithPositionsEvent2          |
      | Win     | Double    | Runner01,Runner11 | 3.00  | 3.00              | 7.59   | 1:Runner01, 2:Runner02, 3:Runner03 | 1:Runner11, 2:Runner12, 3:Runner13 |
      | Eachway | Double    | Runner01,Runner11 | 3.00  | 6.00              | 10.59  | 1:Runner01, 2:Runner02, 3:Runner03 | 1:Runner11, 2:Runner12, 3:Runner13 |