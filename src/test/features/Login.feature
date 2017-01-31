Feature: Login into WagerPlayer

Scenario: Login with valid credentials
  Given the user is on WagerPlayer Login page
  When the user enters username and password
  Then homepage is displayed
