@luxbet @hard-limits
Feature: Hard Limits testing

  Background:
    Given A new default customer with $5000.00 balance is created and logged in API
    When I am logged into WP UI and on Home Page

  Scenario Outline: HARD LIMITS: <interceptAction> Single bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "BALLINA"
    And I create a default Racing event with details
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
    And I update Hard Limit as "15" and also update Max Hard Limit as "20" for runner position "1"
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "<betType>" bet on the runner "<runner>" for $<stake> and "<interceptAction>" the intercept
    Then customer balance is equal to $<balanceBeforeSettlement>

    When I delay test for "60000" milliseconds
    And I navigate to F4 Markets page
    Then event status is "H"

    When I result race with the runners and positions
      | ROCKING HORSE | 1 |
      | COLORADO MISS | 2 |
      | CADEYRN       | 3 |
    And I settle race
    Then customer balance is equal to $<balanceAfterSettlement>

  @smoke
    Examples:
      | interceptAction | betType | runner        | stake  | balanceBeforeSettlement | balanceAfterSettlement |
      | Accept          | Win     | ROCKING HORSE | 400.00 | 4600.00                 | 10600.00               |