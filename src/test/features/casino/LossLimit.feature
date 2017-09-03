@redbook   @cll

Feature: Sunbets Casino Loss Limit

  Background:
    Given A new default customer with $20000.00 balance is created and logged in API
    And I get a PlayTech token for the new customer

  Scenario Outline: Placing multiple spins on <GameType> game with no Casino Loss Limit
    When the loss limit should be $0.00 and loss limit definition should be "24 hours"
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then customer balance is equal to <Balance>

    Examples:
      | GameProvider | GameType | Stake            | Balance   |
      | rhino        | jdean    | 2000.00, 2000.00 | $16000.00 |

  Scenario Outline: Placing multiple spins on <GameType> game with Casino Limit Breached
    When I update the loss limit to $120.00 for 24 hours
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then the message should be "<ErrorMessage>"
    And the error code should be <ErrorCode>

    Examples:
      | GameProvider | GameType | Stake            | ErrorMessage                        | ErrorCode |
      | rhino        | jdean    | 2000.00, 2000.00 | Error : 24 Hour Loss Limit Breached | 129       |

  Scenario Outline: Placing a winning bet in <GameType> game
    And I spin a winning "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then customer balance is equal to <Balance>

    Examples:
      | GameProvider | GameType | Stake            | Balance   |
      | rhino        | jdean    | 2000.00, 2000.00 | $24000.00 |

#   needs to be developed as part of a new pull request
#   Scenario: As an admin user , I should be  able to change the loss limit of a user
#     Then  the admin should be able to update the loss limit to $1200 for 24 hours
#     And the loss limit should be $1200 and loss limit definition should be "24 hours"

  @cv
  Scenario Outline: Place a Sunbets bet after loss limit is breached
    When I update the loss limit to $1200.00 for 24 hours
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Casino Stake>
    And I am logged into WP UI and on Home Page
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 3.30, 4.40, 5.50                     |
    And I enable "<Product>" product settings
      | Betting | Enable Single | Win |
      | Betting | Display Price | Win |
    And I update fixed win prices "<WinPrices>"
    When I place a single Racing "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance is equal to <FinalBalance>

    Examples:
      | Category     | Subcategory   | Product           | BetType | BetOn    | Stake | WinPrices  | GameProvider | GameType | Casino Stake     | FinalBalance |
      | Horse Racing | WOLVERHAMPTON | Luxbook DVP Fixed | Win     | Runner01 | 2.50  | 4.20, 4.10 | rhino        | jdean    | 2000.00, 2000.00 | $17997.50    |

#    When I feed "PA" RabbitMQ with Event message based on "feeds/pa-gh-hove.json"
#    And WagerPlayer receives the Event in "Greyhound Racing"-"Hove"