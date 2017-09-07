#  Need to figure out how it needs to be done
#  Scenario: Use a deposit promo to trigger BonusBuyIn
#    Given A new default customer with $100.00 balance is created and logged in API
#    And I try to get a PlayTech token for the new customer
#
#    When I deposit $100 with the promo code "AAABONUS"
#    Then I should have $50 in real money
#    And I should have $50 in Ringfence/Bonus buy in money
#    And I should have $100 in bonus money
#    And I should have $0 in pending winnings money

# The tests specified in this feature file fail , because the bonus is not being reflected correctly on the  Wagerplayer side of things

@bonus-buy-in
Feature: BonusBuyIn Balance and Loss-Limit

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I get a PlayTech token for the new customer
    And I deposit $100.00 using stored "MC" card using promo code "AAABONUS"

  @cv9
  Scenario Outline: Customer Balance should consider the bonus money after bets are placed
    And I update the loss limit to <LossLimit> for 24 hours
    And customer balance is equal to <Balance>
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then customer balance is equal to <FinalBalance>

    Examples:
      | LossLimit | GameProvider | GameType    | Stake          | Balance | FinalBalance |
      | $40.00    | Playtech     | fatesisters | 40.00          | $150.00 | $150.00      |
      | $40.00    | Playtech     | fatesisters | 100.00, 100.00 | $150.00 | $100.00      |

  Scenario Outline: Casino Bonus is excluded from Casino loss
    And I update the loss limit to <LossLimit> for 24 hours
    And customer balance is equal to <Balance>
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then the message should be "<ErrorMessage>"
    And the error code should be <ErrorCode>

    Examples:
      | LossLimit | GameProvider | GameType    | Stake               | Balance | ErrorMessage                        | ErrorCode |
      | $40.00    | Playtech     | fatesisters | 150.00, 40.00, 3.00 | $150.00 | Error : 24 Hour Loss Limit Breached | 129       |
      | $40.00    | Playtech     | fatesisters | 200.00, 1.00        | $150.00 | Error : 24 Hour Loss Limit Breached | 129       |
      | $40.00    | Playtech     | fatesisters | 80.00               | $150.00 |                                     | 0         |

