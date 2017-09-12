#all the bets placed only use real money and do not use the bonus money

@bonus-buy-in
Feature: BonusBuyIn Balance and Loss-Limit

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I get a PlayTech token for the new customer
    And I deposit $100.00 using stored "MC" card using promo code "JAGBONUS"
    Then customer balance is equal to $150.00

  @r1
  Scenario Outline:  Should have the correct amount for individual bonus components
    And the Bonus Balance should be <BonusBalance>
    And the Ringfenced Balance should be <RingfencedBalance>
    And the Bonus Pending Winnings should be <PendingWinningsBalance>

    Examples:
      | RingfencedBalance | BonusBalance | PendingWinningsBalance |
      | $50.00            | $100.00      | $0.00                  |

  Scenario Outline: Customer Balance should consider the bonus money after bets are placed
    And the Bonus Balance should be <BonusBalance>
    When I update the loss limit to <LossLimit> for 24 hours
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then customer balance is equal to <FinalBalance>

    Examples:
      | LossLimit | GameProvider | GameType    | Stake       | BonusBalance | FinalBalance |
      | $60.00    | Playtech     | fatesisters | 40.00,10.00 | $100.00      | $100.00      |

    # Scenario not used because placing multiple real balance bets instead of bets using promo balance.
    # | $40.00    | Playtech     | fatesisters | 100.00, 100.00 | $150.00 | $100.00      | $100.00      |

  Scenario Outline: Casino Bonus is excluded from Casino loss
    When I update the loss limit to <LossLimit> for 24 hours
    And I place multiple spins on "<GameProvider>" Casino "<GameType>" game with a stake of <Stake>
    Then the error code should be <ErrorCode>
    And the message should be "<ErrorMessage>"

    Examples:
      | LossLimit | GameProvider | GameType    | Stake              | ErrorMessage                        | ErrorCode |
      | $40.00    | Playtech     | fatesisters | 200.00             | Warning : Insufficent funds         | 108       |
      | $40.00    | Playtech     | fatesisters | 10.00, 40.00, 3.00 | Error : 24 Hour Loss Limit Breached | 129       |
      | $40.00    | Playtech     | fatesisters | 80.00              |                                     | 0         |
