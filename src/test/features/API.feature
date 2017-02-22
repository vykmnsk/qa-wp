@rest
Feature: REST API

  @wip
  Scenario: WAPI
    When I login to WAPI
    Then I get WAPI session ID

    Given Customer balance is greater than $20