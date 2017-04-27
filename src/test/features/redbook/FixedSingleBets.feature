@redbook @single-bets @fixed-single-bets

Feature: Placing and Settling multiple Single bets on a Sunbets event

  Background:
    Given I am logged into WP API
    And I am logged into WP UI and on Home Page

  Scenario: Horse Race multiple Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners   | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices    | 1.10, 2.20, 2.80, 3.40, 5.20                     |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | EW    |
    And customer balance is at least $20.50

    When I place a single "Win" bet on the runner "Runner01" for $5.50
    And I place a single "Win" bet on the runner "Runner01" for $3.50
    And I place a single "Eachway" bet on the runner "Runner02" for $5.50
    And I place a single "Eachway" bet on the runner "Runner03" for $2.50
    Then customer balance is decreased by $25.00

    When I result "WOLVERHAMPTON" race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |

    And I settle race
    Then customer balance is increased by $9.90