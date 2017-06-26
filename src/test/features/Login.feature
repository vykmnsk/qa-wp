@login
Feature: Login into WagerPlayer

  Scenario: API Login
    Given A new default customer with $100.00 balance is created and logged in API

  Scenario: UI Login with valid credentials
    When I login with valid username and password
    Then homepage is displayed

  Scenario: UI Login with invalid credentials
    When I login with invalid username and password
    Then I see "Invalid user" message

