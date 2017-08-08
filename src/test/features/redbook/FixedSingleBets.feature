@redbook @several-single-bets

Feature: Placing and Settling several Single bets on the same event

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  @smoke
  Scenario: Horse Race several Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 2.80, 3.40, 5.20                     |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win |
      | Betting | Enable Single | EW  |

    When I place a single Racing "Win" bet on the runner "Runner01" for $5.50
    And I place a single Racing "Win" bet on the runner "Runner01" for $3.50
    And I place a single Racing "Eachway" bet on the runner "Runner02" for $5.50
    And I place a single Racing "Eachway" bet on the runner "Runner03" for $2.50
    Then customer balance after bet is decreased by $25.00

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance since last bet is increased by $9.90