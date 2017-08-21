@redbook   @cll

Feature: Sunbets Casino Loss Limit

  Background:
    Given A new default customer with $20000.00 balance is created and logged in API
    And I try to get a PlayTech token for the new customer

  Scenario Outline: Placing two spins on <GameType> game with no Casino Loss Limit
    When the loss limit should be $0.00 and loss limit definition should be "24 hours"
    And I spin a "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    And I spin a "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then customer balance is equal to <Balance>

    Examples:
      | GameProvider | GameType | Stake    | Balance   |
      | rhino        | jdean    | $2000.00 | $16000.00 |

  @cv9
  Scenario Outline: Placing two spins on <GameType> game with Casino Limit Breached
    When I update the loss limit to $120.00 for 24 hours
    And I spin a "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    And I spin a "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then the message should be "<ErrorMessage>"
    And the error code should be <ErrorCode>

    Examples:
      | GameProvider | GameType | Stake    | ErrorMessage                        | ErrorCode |
      | rhino        | jdean    | $2000.00 | Error : 24 Hour Loss Limit Breached | 129       |

#   needs to be developed as part of a new pull request
#   Scenario: As an admin user , I should be  able to change the loss limit of a user
#     Then  the admin should be able to update the loss limit to $1200 for 24 hours
#     And the loss limit should be $1200 and loss limit definition should be "24 hours"
