Feature: End 2 End

  Background:
    Given I am logged in and on Home Page

  @wip
  Scenario Outline: Horse Race Single Win bet
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | base name | TEST RACE          |
      | runners   | Runner01, Runner02 |
      | prices    | 1.10, 2.20         |
    And I enable "<ProductName>" product settings
      | Betting | Enabled       | On  |
      | Betting | Enable Single | Win |

    Given I am logged in WAPI
    And customer balance is at least $20.50
    When I place a single "Win" bet on the runner "Runner01" for $2.50
    Then customer balance is decreased by $2.50

    When I collect mpid of selections
    When I settle race with the runners and positions
      | Runner01  | 1        |
      | Runner02  | 2        |
    Then customer balance is increased by $2.75

  @luxbet
    Examples:
      | ProductName       |
      | Luxbook DVP Fixed |

  @redbook
    Examples:
      | ProductName |
      | PA SP       |
