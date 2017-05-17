@luxbet @deductions
Feature: Create and verify event in Luxbet WagerPlayer

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario: Deductions for 3 place 1 scratched runner
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 1.12, 1.40, 14.00, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | Place |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
      | Liability | Single        | Win   |
      | Liability | Single        | Place |
      | Liability | Base          | Win   |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | 1/4       |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "Place" bet on the runner "ROCKING HORSE" for $4.00
    And I place a single "Win" bet on the runner "COLORADO MISS" for $4.00
    And I place a single "Eachway" bet on the runner "CADEYRN" for $4.00
    And I place a single "Win" bet on the runner "PROSPECT ROAD" for $4.00
    And I place a single "Place" bet on the runner "WHITE LADY" for $4.00
    And I place a single "Eachway" bet on the runner "SUPERBEE" for $4.00
    And I place a single "Win" bet on the runner "FIGHT FOR GLORY" for $4.00
    And I place a single "Eachway" bet on the runner "BONUS SPIN" for $4.00
    And I place a single "Place" bet on the runner "TORCHBEARER" for $4.00
    And I place a single "Win" bet on the runner "TRUST ME" for $4.00
    Then customer balance is equal to $48.00

    When I scratch the runners at position(s) "1"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 69 | 29 |
    When I result race with the runners and positions
      | COLORADO MISS | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I settle race
    Then customer balance is equal to $68.22

  Scenario: Deductions for 2 place 1 scratched runner
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 1.12, 1.40, 14.00, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | 1/4       |
      | No of Places       | 2         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "Place" bet on the runner "ROCKING HORSE" for $4.00
    And I place a single "Win" bet on the runner "COLORADO MISS" for $4.00
    And I place a single "Eachway" bet on the runner "CADEYRN" for $4.00
    And I place a single "Win" bet on the runner "PROSPECT ROAD" for $4.00
    And I place a single "Place" bet on the runner "WHITE LADY" for $4.00
    And I place a single "Eachway" bet on the runner "SUPERBEE" for $4.00
    And I place a single "Win" bet on the runner "FIGHT FOR GLORY" for $4.00
    And I place a single "Eachway" bet on the runner "BONUS SPIN" for $4.00
    And I place a single "Place" bet on the runner "TORCHBEARER" for $4.00
    And I place a single "Win" bet on the runner "TRUST ME" for $4.00
    Then customer balance is equal to $48.00

    When I scratch the runners at position(s) "1"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 69 | 42 |
    When I result race with the runners and positions
      | COLORADO MISS | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I settle race
    Then customer balance is equal to $58.96

  Scenario: Deductions for 3 place 2 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 2.28, 2.35, 14.00, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | Place |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
      | Liability | Single        | Win   |
      | Liability | Single        | Place |
      | Liability | Base          | Win   |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | 1/4       |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "Place" bet on the runner "ROCKING HORSE" for $4.00
    And I place a single "Win" bet on the runner "COLORADO MISS" for $4.00
    And I place a single "Eachway" bet on the runner "CADEYRN" for $4.00
    And I place a single "Win" bet on the runner "PROSPECT ROAD" for $4.00
    And I place a single "Place" bet on the runner "WHITE LADY" for $4.00
    And I place a single "Eachway" bet on the runner "SUPERBEE" for $4.00
    And I place a single "Win" bet on the runner "FIGHT FOR GLORY" for $4.00
    And I place a single "Eachway" bet on the runner "BONUS SPIN" for $4.00
    And I place a single "Place" bet on the runner "TORCHBEARER" for $4.00
    And I place a single "Win" bet on the runner "TRUST ME" for $4.00
    Then customer balance is equal to $48.00

    When I scratch the runners at position(s) "1,2"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 21 |
      | 2 COLORADO MISS | 32 | 21 |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I settle race
    Then customer balance is equal to $86.03