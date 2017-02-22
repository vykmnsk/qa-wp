@rest
Feature: REST API

  Scenario: WAPI
    When I login to WAPI
    Then I get WAPI session ID