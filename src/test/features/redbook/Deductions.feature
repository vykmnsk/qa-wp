@deductions @redbook
Feature: This is to verify deductions is applied and bets are payed out correctly

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Deductions for Racing 1 scratched runner
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 1.12, 1.40, 14.00, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                 |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |

    When I place a single "Win" bet on the runner "Runner01" for $4.00
    And I place a single "Win" bet on the runner "Runner02" for $4.00
    And I place a single "Eachway" bet on the runner "Runner03" for $4.00
    And I place a single "Win" bet on the runner "Runner04" for $4.00
    And I place a single "Win" bet on the runner "Runner05" for $4.00
    And I place a single "Eachway" bet on the runner "Runner06" for $4.00
    And I place a single "Win" bet on the runner "Runner07" for $4.00
    And I place a single "Eachway" bet on the runner "Runner08" for $4.00
    And I place a single "Eachway" bet on the runner "Runner09" for $4.00
    And I place a single "Win" bet on the runner "Runner10" for $4.00
    Then customer balance is equal to $44.00

    When I scratch the runners at position(s) "1"
    Then I can see the following deduction details on settlement page
      | 1 Runner01 | 85 | 85 |
    When I result race with the runners and positions
      | Runner02 | 1 |
      | Runner06 | 2 |
      | Runner08 | 3 |
    And I settle race
    Then I verify status and payout of bets placed on runners
      | Runner01 | Refunded    | 4.00 |
      | Runner02 | Punter Win  | 4.24 |
      | Runner03 | Punter Loss | 0.00 |
      | Runner04 | Punter Loss | 0.00 |
      | Runner05 | Punter Loss | 0.00 |
      | Runner06 | Punter Win  | 7.75 |
      | Runner07 | Punter Loss | 0.00 |
      | Runner08 | Punter Win  | 6.10 |
      | Runner09 | Punter Loss | 0.00 |
      | Runner10 | Punter Loss | 0.00 |
    And customer balance is equal to $66.09

    Examples:
      | Category         | Subcategory   |
      | Horse Racing     | WOLVERHAMPTON |

  Scenario Outline: <Category> Maximum deductions is 90 Pence
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 2.00, 1.40, 14.00, 1.80, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                  |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | EW    |
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |

    When I place a single "Win" bet on the runner "Runner01" for $4.00
    And I place a single "Win" bet on the runner "Runner02" for $4.00
    When I scratch the runners at position(s) "1"
    And I place a single "Eachway" bet on the runner "Runner03" for $4.00
    And I place a single "Win" bet on the runner "Runner04" for $4.00
    And I place a single "Win" bet on the runner "Runner05" for $4.00
    And I place a single "Eachway" bet on the runner "Runner06" for $4.00
    When I scratch the runners at position(s) "4"
    And I place a single "Win" bet on the runner "Runner07" for $4.00
    And I place a single "Eachway" bet on the runner "Runner08" for $4.00
    And I place a single "Eachway" bet on the runner "Runner09" for $4.00
    And I place a single "Win" bet on the runner "Runner10" for $4.00
    Then customer balance is equal to $52.00

   Then I can see the following deduction details on settlement page
      | 1 Runner01 | 45 | 45 |
      | 4 Runner04 | 55 | 55 |
    When I result race with the runners and positions
      | Runner02 | 1 |
      | Runner06 | 2 |
      | Runner08 | 3 |
    And I settle race
    Then I verify status and payout of bets placed on runners
      | Runner01        | Refunded    | 4.00  |
      | Runner02        | Punter Win  | 4.16  |
      | Runner03        | Punter Loss | 0.00  |
      | Runner04        | Refunded    | 4.00  |
      | Runner05        | Punter Loss | 0.00  |
      | Runner06        | Punter Win  | 15.25 |
      | Runner07        | Punter Loss | 0.00  |
      | Runner08        | Punter Win  | 18.00 |
      | Runner09        | Punter Loss | 0.00  |
      | Runner10        | Punter Loss | 0.00  |
    And customer balance is equal to $89.41

    Examples:

      | Category         | Subcategory    |
      | Horse Racing     | WOLVERHAMPTON  |
      | Greyhound Racing | RICHMOND       |

