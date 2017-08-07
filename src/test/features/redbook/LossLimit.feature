@redbook @loss-limit

Feature: Casino Loss Limit

  Background:
    Given A new default customer with $100.00 balance is created and logged in API

  Scenario: The Default Loss limit should be 0
    Then the loss limit should be $0.0 and loss limit definition should be "24 hours"

  @set-limit
  Scenario: Set Loss Limit and Loss Limit Definition. Ensure it is updated
    When I update the loss limit to $1200 for 48 hours
    Then the loss limit should be $1200 and loss limit definition should be "48 hours"