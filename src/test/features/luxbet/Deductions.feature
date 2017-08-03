@luxbet @deductions
Feature: Create and verify event in Luxbet WagerPlayer

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  @smoke
  Scenario: Deductions for 3 place 1 scratched runner
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
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
    And I result race with Fixed Win prices "1.40, 10.00, 15.00" and Place prices "1.10, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00 |
      | COLORADO MISS   | Punter Win  | 1.74 |
      | CADEYRN         | Punter Loss | 0.00 |
      | PROSPECT ROAD   | Punter Loss | 0.00 |
      | WHITE LADY      | Punter Win  | 6.39 |
      | SUPERBEE        | Punter Loss | 0.00 |
      | FIGHT FOR GLORY | Punter Loss | 0.00 |
      | BONUS SPIN      | Punter Win  | 8.09 |
      | TORCHBEARER     | Punter Loss | 0.00 |
      | TRUST ME        | Punter Loss | 0.00 |
    And customer balance is equal to $68.22

  Scenario: Deductions for 2 place 1 scratched runner
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
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
    And I result race with Fixed Win prices "1.40, 10.00, 15.00" and Place prices "1.10, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00 |
      | COLORADO MISS   | Punter Win  | 1.74 |
      | CADEYRN         | Punter Loss | 0.00 |
      | PROSPECT ROAD   | Punter Loss | 0.00 |
      | WHITE LADY      | Punter Win  | 5.22 |
      | SUPERBEE        | Punter Loss | 0.00 |
      | FIGHT FOR GLORY | Punter Loss | 0.00 |
      | BONUS SPIN      | Punter Loss | 0.00 |
      | TORCHBEARER     | Punter Loss | 0.00 |
      | TRUST ME        | Punter Loss | 0.00 |
    And customer balance is equal to $58.96

  Scenario: Deductions for 3 place 2 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
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
    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00  |
      | COLORADO MISS   | Refunded    | 4.00  |
      | CADEYRN         | Punter Loss | 0.00  |
      | PROSPECT ROAD   | Punter Win  | 18.20 |
      | WHITE LADY      | Punter Win  | 5.22  |
      | SUPERBEE        | Punter Loss | 0.00  |
      | FIGHT FOR GLORY | Punter Loss | 0.00  |
      | BONUS SPIN      | Punter Win  | 6.61  |
      | TORCHBEARER     | Punter Loss | 0.00  |
      | TRUST ME        | Punter Loss | 0.00  |
    And customer balance is equal to $86.03

  Scenario: Deductions for 2 place 2 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
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

    When I scratch the runners at position(s) "1,2"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 29 |
      | 2 COLORADO MISS | 32 | 28 |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00  |
      | COLORADO MISS   | Refunded    | 4.00  |
      | CADEYRN         | Punter Loss | 0.00  |
      | PROSPECT ROAD   | Punter Win  | 18.20 |
      | WHITE LADY      | Punter Win  | 3.87  |
      | SUPERBEE        | Punter Loss | 0.00  |
      | FIGHT FOR GLORY | Punter Loss | 0.00  |
      | BONUS SPIN      | Punter Loss | 0.00  |
      | TORCHBEARER     | Punter Loss | 0.00  |
      | TRUST ME        | Punter Loss | 0.00  |
    And customer balance is equal to $78.07

  Scenario: Deductions for 3 place 3 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 2.28, 2.35, 4.12, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                              |
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

    When I scratch the runners at position(s) "1,2,3"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 21 |
      | 2 COLORADO MISS | 32 | 21 |
      | 3 CADEYRN       | 14 | 12 |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |

    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00  |
      | COLORADO MISS   | Refunded    | 4.00  |
      | CADEYRN         | Refunded    | 8.00  |
      | PROSPECT ROAD   | Punter Win  | 10.92 |
      | WHITE LADY      | Punter Win  | 4.14  |
      | SUPERBEE        | Punter Loss | 0.00  |
      | FIGHT FOR GLORY | Punter Loss | 0.00  |
      | BONUS SPIN      | Punter Win  | 5.24  |
      | TORCHBEARER     | Punter Loss | 0.00  |
      | TRUST ME        | Punter Loss | 0.00  |
    And customer balance is equal to $84.30

  Scenario: Deductions for 2 place 3 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 2.28, 2.35, 4.12, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                              |
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

    When I scratch the runners at position(s) "1,2,3"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 29 |
      | 2 COLORADO MISS | 32 | 28 |
      | 3 CADEYRN       | 14 | 16 |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00  |
      | COLORADO MISS   | Refunded    | 4.00  |
      | CADEYRN         | Refunded    | 8.00  |
      | PROSPECT ROAD   | Punter Win  | 10.92 |
      | WHITE LADY      | Punter Win  | 2.43  |
      | SUPERBEE        | Punter Loss | 0.00  |
      | FIGHT FOR GLORY | Punter Loss | 0.00  |
      | BONUS SPIN      | Punter Loss | 0.00  |
      | TORCHBEARER     | Punter Loss | 0.00  |
      | TRUST ME        | Punter Loss | 0.00  |
    And customer balance is equal to $77.35

  Scenario: Deductions for 3 place 4 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 2.28, 2.35, 4.12, 13.00, 10.00, 7.60, 35.00, 15.00, 61.00, 23.00                                                               |
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

    When I scratch the runners at position(s) "1,2,3,6"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 21 |
      | 2 COLORADO MISS | 32 | 21 |
      | 3 CADEYRN       | 14 | 12 |
      | 6 SUPERBEE      | 7  | 7  |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00 |
      | COLORADO MISS   | Refunded    | 4.00 |
      | CADEYRN         | Refunded    | 8.00 |
      | PROSPECT ROAD   | Punter Win  | 7.28 |
      | WHITE LADY      | Punter Win  | 3.51 |
      | SUPERBEE        | Refunded    | 8.00 |
      | FIGHT FOR GLORY | Punter Loss | 0.00 |
      | BONUS SPIN      | Punter Win  | 4.45 |
      | TORCHBEARER     | Punter Loss | 0.00 |
      | TRUST ME        | Punter Loss | 0.00 |
    And customer balance is equal to $87.24

  Scenario: Deductions for 2 place 4 scratched runners
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 2.28, 2.35, 4.12, 13.00, 10.00, 7.60, 35.00, 15.00, 61.00, 23.00                                                               |
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

    When I scratch the runners at position(s) "1,2,3,6"
    Then I can see the following deduction details on settlement page
      | 1 ROCKING HORSE | 33 | 29 |
      | 2 COLORADO MISS | 32 | 28 |
      | 3 CADEYRN       | 14 | 16 |
      | 6 SUPERBEE      | 7  | 9  |
    When I result race with the runners and positions
      | PROSPECT ROAD | 1 |
      | WHITE LADY    | 2 |
      | BONUS SPIN    | 3 |
    And I result race with Fixed Win prices "13.00, 10.00, 15.00" and Place prices "2.60, 2.25, 2.85"
    And I settle race
    Then I verify status and payout of bets placed on runners
      | ROCKING HORSE   | Refunded    | 4.00 |
      | COLORADO MISS   | Refunded    | 4.00 |
      | CADEYRN         | Refunded    | 8.00 |
      | PROSPECT ROAD   | Punter Win  | 7.28 |
      | WHITE LADY      | Punter Win  | 1.62 |
      | SUPERBEE        | Refunded    | 8.00 |
      | FIGHT FOR GLORY | Punter Loss | 0.00 |
      | BONUS SPIN      | Punter Loss | 0.00 |
      | TORCHBEARER     | Punter Loss | 0.00 |
      | TRUST ME        | Punter Loss | 0.00 |
    And customer balance is equal to $80.90