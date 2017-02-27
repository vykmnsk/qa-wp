@rest
Feature: REST API

  Scenario: WAPI
    Given I am logged in WAPI
    And customer balance is at least $20.50
    When I place a single Win bet on the first runner for $2.50
    Then customer balance changes

