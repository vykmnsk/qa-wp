Feature: Login into WagerPlayer

  Scenario: Login with valid credentials
    When I login with valid username and password
    Then homepage is displayed

  Scenario: Login with invalid credentials
    When I login with invalid username and password
    Then I see "Invalid user" message

