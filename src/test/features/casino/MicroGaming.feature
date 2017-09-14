@micro-gaming
Feature: Microgaming

  Scenario: Should get AccessToken successfully
    Given A new default customer with $100.00 balance is created and logged in API
    And I get a Microgaming token for the customer successfully
    When I place a Microgaming bet for $50.00
    Then customer balance is equal to $50.00