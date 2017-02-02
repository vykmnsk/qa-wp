Feature: Login into WagerPlayer

  Scenario: Login with valid credentials
    Given the user is on WagerPlayer Login page
    When the user enters username and password
    Then homepage is displayed

  Scenario: Login with invalid credentials
    When I login with wrong username and password
    Then I see "Invalid user" message