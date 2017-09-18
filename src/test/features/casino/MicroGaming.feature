@micro-gaming
Feature: Microgaming

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I get a Microgaming token for the customer successfully

  Scenario: Should be able to place a Microgaming bet successfully
    When I place multiple Microgaming bets for 50.00,10.00
    Then customer balance is equal to $40.00

  Scenario: Should be able to place a Microgaming win bet successfully
    When I place a Microgaming bet for $50.00 and expecting to win of $90.00
    Then customer balance is equal to $140.00

  Scenario Outline: Should trigger Loss Limit when loss limit is breached
    When I update the loss limit to $40.00 for 24 hours
    And I place multiple Microgaming bets for <Stakes>
    Then the <ProviderType> error message should be <ErrorMessage>
    And the <ProviderType> error code should be <ErrorCode>

    Examples:
      | ProviderType | Stakes | ErrorMessage                     | ErrorCode |
      | Microgaming  | 50,30  | Customer has exceeded loss limit | 6509      |