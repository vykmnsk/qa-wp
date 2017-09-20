@gsi
Feature: GSI Integration

  Scenario Outline: Should successfully get a GSI token
    Given A new default customer with $100.00 balance is created and logged in API
    Then I get a <ProviderType> token for the customer successfully

    Examples:
      | ProviderType |
      | GSI          |
      | Microgaming  |
      | PlayTech     |
    