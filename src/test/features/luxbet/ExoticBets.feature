@exotic-bets @luxbet
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Exotic bets
    When I enter specifics category "Horse Racing" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.10, 2.20, 1.20, 2.40, 1.30, 2.60, 1.40, 2.80                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
    And I enable "<ProductName>" product settings
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And customer balance is at least $20.50

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    Examples:
      | Subcategory   | ProductName     | BetType    | BetOn                               | Stake | Flexi | BalanceDeductedBy |
      | BALLINA       | STAB Quinella   | Quinella   | Runner01,Runner02                   | 3.00  | N     | 3.00              |
      | BALLINA       | STAB Exacta     | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00              |
      | BALLINA       | STAB Trifecta   | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00             |
      | BALLINA       | STAB First Four | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00             |
      | BALLINA       | NSW First Four  | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00             |
      | WOLVERHAMPTON | NSW Quinella    | Quinella   | Runner01,Runner02                   | 3.00  | N     | 3.00              |
      | WOLVERHAMPTON | NSW Exacta      | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00              |
      | WOLVERHAMPTON | NSW Trifecta    | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00             |