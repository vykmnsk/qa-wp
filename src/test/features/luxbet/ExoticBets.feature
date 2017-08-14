@exotic-bets @luxbet
Feature: Placing and Settling Exotic Bets for a Single Event

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page
    And I update the customer AML status to "Manually Verified"

  Scenario Outline: Horse Race Exotic bets
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 12.00, 126.00, 3.50, 21.00, 26.00, 3.80, 8.00, 4.40                            |
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
    And I update fixed place prices "3.20, 21.85, 1.55, 4.75, 5.60, 1.65, 1.80, 1.85" for the first product

    When I place an exotic "<BetType>" bet on the runners "<BetOn>" for $<Stake> with flexi as "<Flexi>"
    Then customer balance after bet is decreased by $<Cost>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
      | Runner04 | 4 |
    And I update Exotic Prices
      | STAB | Quinella   | 6.50  |
      | STAB | Exacta     | 12.75 |
      | STAB | Trifecta   | 25.45 |
      | STAB | First Four | 35.00 |
    And I settle race
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Category     | Subcategory | ProductName   | BetType  | BetOn             | Stake | Flexi | Cost | Payout |
      | Horse Racing | BALLINA     | STAB Quinella | Quinella | Runner01,Runner02 | 3.00  | N     | 3.00 | 19.50  |

    Examples:
      | Category       | Subcategory | ProductName     | BetType    | BetOn                               | Stake | Flexi | Cost  | Payout |
      | Horse Racing   | BALLINA     | STAB Exacta     | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00  | 38.25  |
      | Horse Racing   | BALLINA     | STAB Trifecta   | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00 | 76.35  |
      | Horse Racing   | BALLINA     | STAB First Four | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00 | 105.00 |
      | Harness Racing | ALBANY      | STAB Quinella   | Quinella   | Runner01,Runner02                   | 3.00  | N     | 3.00  | 19.50  |
      | Harness Racing | ALBANY      | STAB Exacta     | Exacta     | Runner01,Runner02                   | 3.00  | N     | 6.00  | 38.25  |
      | Harness Racing | ALBANY      | STAB Trifecta   | Trifecta   | Runner01,Runner02,Runner03          | 3.00  | N     | 18.00 | 76.35  |
      | Harness Racing | ALBANY      | STAB First Four | First Four | Runner01,Runner02,Runner03,Runner04 | 3.00  | Y     | 72.00 | 105.00 |