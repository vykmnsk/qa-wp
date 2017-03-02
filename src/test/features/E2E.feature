Feature: End 2 End

  Background:
    Given I am logged in and on Home Page

  Scenario: Horse Race Single Win bet
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name        | TEST RACE          |
      | runners          | runner1, runner2   |
      | prices             | 1.10, 2.20         |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting   | Enabled        | On    |
      | Betting   | Enable Single  | Win   |

    Given I am logged in WAPI
    And customer balance is at least $20.50
    When I place a single Win bet on the first runner for $2.50
    Then customer balance is decreased



