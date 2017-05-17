@luxbet @single-bets @fixed-single-bets
Feature: Placing and Settling multiple Single bets on a Luxbet event

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Racing multiple Single bets
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00                                                           |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "Win" bet on the runner "ROCKING HORSE" for $5.50
    And I place a single "Win" bet on the runner "ROCKING HORSE" for $3.50
    And I place a single "Win" bet on the runner "WHITE LADY" for $3.50
    When I place a single "Place" bet on the runner "ROCKING HORSE" for $2.50
    And I place a single "Eachway" bet on the runner "COLORADO MISS" for $5.50
    And I place a single "Eachway" bet on the runner "CADEYRN" for $2.50
    Then customer balance is equal to $69.00

    When I result race with the runners and positions
      | ROCKING HORSE | 1 |
      | COLORADO MISS | 2 |
      | CADEYRN       | 3 |
    And I settle race
    Then customer balance is equal to $223.93

    Examples:
      | Category         | Subcategory                 |
      | Horse Racing     | Automation Horse Racing     |
      | GREYHOUND RACING | Automation Greyhound Racing |
      | Harness Racing   | Automation Harness Racing   |