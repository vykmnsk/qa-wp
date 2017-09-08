
Feature: Microgaming

  @er
  Scenario: Should get AccessToken successfully
    Given A new default customer with $100.00 balance is created and logged in API
    Then I should get a Microgaming token for the customer successfully
