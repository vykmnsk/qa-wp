@exotic-bets @luxbet
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given I am logged into WP UI and on Home Page
    And I am logged into WP API

  Scenario Outline: Horse Race Exotic bets
    When I enter specifics category "Horse Racing" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner 06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 12.00, 126.00, 3.50, 21.00, 26.00, 3.80, 8.00, 4.40, 5.00, 10.00                                    |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
    And I enable "<ProductName>" product settings
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices
      | placePrices | 3.20, 21.85, 1.55, 4.75, 5.60, 1.65, 1.80, 1.85, 1.85, 2.85 |
    And customer balance is at least $20.50

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
      | Runner04 | 4 |
    And I settle race
    Then customer balance is increased by $<Payout>

    Examples:
      | Subcategory   | ProductName     | BetType    | BetOn                               | Stake | Flexi | BalanceDeductedBy | Payout |
      | BALLINA       | STAB Quinella   | Quinella   | Runner01,Runner02                   | 3.00  | N     | 3.00              | 0.00   |
      | BALLINA       | STAB Exacta     | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00              | 0.00   |
      | BALLINA       | STAB Trifecta   | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00             | 0.00   |
      | BALLINA       | STAB First Four | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00             | 0.00   |
      | BALLINA       | NSW First Four  | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00             | 0.00   |
      | WOLVERHAMPTON | NSW Quinella    | Quinella   | Runner01,Runner02                   | 3.00  | N     | 3.00              | 0.00   |
      | WOLVERHAMPTON | NSW Exacta      | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00              | 0.00   |
      | WOLVERHAMPTON | NSW Trifecta    | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00             | 0.00   |