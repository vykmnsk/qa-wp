@luxbet @dead-heats
Feature: Deadheats testing on a Luxbet event

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page
    And I update the customer AML status to "Manually Verified"

  Scenario Outline: Deadheat at <type>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | <WinPrices>                                                                                                                    |
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
    And I update fixed place prices "<PlacePrices>"

    When I place a single Racing "Win" bet on the runner "ROCKING HORSE" for $5.50
    And I place a single Racing "Win" bet on the runner "WHITE LADY" for $3.50
    When I place a single Racing "Place" bet on the runner "ROCKING HORSE" for $2.50
    And I place a single Racing "Eachway" bet on the runner "COLORADO MISS" for $5.50
    And I place a single Racing "Eachway" bet on the runner "CADEYRN" for $2.50
    And I place a single Racing "Eachway" bet on the runner "PROSPECT ROAD" for $2.50
    Then customer balance after bet is decreased by $32.50

    When I result race with the runners and positions "<WinnerWithPositions>"
    And I settle race
    Then customer balance since last bet is increased by $<Payout>

    @smoke
    Examples:
      | type   | Category         | Subcategory             | WinPrices                                                            | PlacePrices                                                | WinnerWithPositions                                          | Payout |
      | FIRST  | Horse Racing     | Automation Horse Racing | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 1:COLORADO MISS, 3:CADEYRN, 4:PROSPECT ROAD | 65.03  |

    Examples:
      | type   | Category         | Subcategory             | WinPrices                                                            | PlacePrices                                                | WinnerWithPositions                                          | Payout |
      | FIRST  | Greyhound Racing | CORK                    | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 1:COLORADO MISS, 3:CADEYRN, 4:PROSPECT ROAD | 65.03  |
      | FIRST  | Harness Racing   | ALBION PARK             | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 1:COLORADO MISS, 3:CADEYRN, 4:PROSPECT ROAD | 65.03  |
      | SECOND | Horse Racing     | Automation Horse Racing | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 2:CADEYRN, 4:PROSPECT ROAD | 102.43 |
      | SECOND | Greyhound Racing | CORK                    | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 2:CADEYRN, 4:PROSPECT ROAD | 102.43 |
      | SECOND | Harness Racing   | ALBION PARK             | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 2:CADEYRN, 4:PROSPECT ROAD | 102.43 |
      | THIRD  | Horse Racing     | Automation Horse Racing | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 3:CADEYRN, 3:PROSPECT ROAD | 102.31 |
      | THIRD  | Greyhound Racing | CORK                    | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 3:CADEYRN, 3:PROSPECT ROAD | 102.31 |
      | THIRD  | Harness Racing   | ALBION PARK             | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 1:ROCKING HORSE, 2:COLORADO MISS, 3:CADEYRN, 3:PROSPECT ROAD | 102.31 |