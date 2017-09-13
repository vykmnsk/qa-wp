@redbook @colossus

Feature: Placing and Settling Colossus Bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API

  @col-place
  Scenario Outline: Placing a Colossus <Class> Bet
    When I place an External bet for $<Stake>
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |
      | bet status      | <PlaceStatus>    |

    And I verify customer statement via API
      | service name  | <ServiceName>    |
      | type          | <Type>           |
      | debit amount  | <DebitAmount>    |
      | credit amount | <CreditAmount>   |
      | balance       | <Balance>        |
      | description   | <BetDescription> |

    Then customer balance is equal to $<Balance>

    Examples:
      | TestCase           | ExtId    | Class  | Stake  | Balance | ServiceName | Type             | BetType | BetDescription                | DebitAmount | CreditAmount | PlaceStatus |
      | SUN-5561, SUN-5594 | Auto-Gen | Normal | 100.00 | 0.00    | Colossus    | Sun Jackpots Bet | 0       | Place Colossus Bet Automation | 100.00      | 0.00         | Accepted    |
      | SUN-5561, SUN-5594 | Auto-Gen | Normal | 20.22  | 79.78   | Colossus    | Sun Jackpots Bet | 0       | Place Colossus Bet Automation | 20.22       | 0.00         | Accepted    |
      | SUN-5561, SUN-5594 | Auto-Gen | Normal | 0.10   | 99.90   | Colossus    | Sun Jackpots Bet |         | Place Colossus Bet Automation | 0.10        | 0.00         | Accepted    |
      | SUN-5670           | Auto-Gen | Free   | 50.00  | 100.00  | Colossus    | Sun Jackpots Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         | Accepted    |
      | SUN-6147           | Auto-Gen | Free   | 200.00 | 100.00  | Colossus    | Sun Jackpots Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         | Accepted    |
      |                    | Auto-Gen | Other  | 50.00  | 50.00   | Colossus    | Sun Jackpots Bet | 5       | Place Colossus Bet Automation | 50.00       | 0.00         | Accepted    |
      | Regression         | Auto-Gen | Other  | 55.00  | 45.00   | red7        | Tertiary Bet     |         | Regression External Place Bet | 55.00       | 0.00         | Accepted    |
      | Regression         | Auto-Gen | Free   | 200.00 | 100.00  | red7        | Tertiary Bet     | 1       | Regression External Free Bet  | 0.00        | 0.00         | Accepted    |

  @col-cancel
  Scenario Outline: Placing a Colossus <Class> Bet
    When I place an External bet for $<Stake>
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |
      | bet status      | <PlaceStatus>    |

    And I verify customer statement via API
      | service name  | <ServiceName>    |
      | type          | <Type>           |
      | debit amount  | <DebitAmount>    |
      | credit amount | <CreditAmount>   |
      | balance       | <Balance>        |
      | description   | <BetDescription> |

    Then customer balance is equal to $<Balance>

    When I cancel an External bet
      | service name | <ServiceName>  |
      | cancel note  | <CancelNote>   |
      | bet status   | <CancelStatus> |

    Then customer balance is equal to $100.00


    Examples:
      | TestCase   | ExtId    | Class  | Stake  | Balance | ServiceName | Type             | BetType | BetDescription                | DebitAmount | CreditAmount | PlaceStatus | CancelStatus | CancelNote                        |
      | SUN-5617   | Auto-Gen | Normal | 100.00 | 0.00    | Colossus    | Sun Jackpots Bet | 0       | Place Colossus Bet Automation | 100.00      | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | SUN-5617   | Auto-Gen | Normal | 20.22  | 79.78   | Colossus    | Sun Jackpots Bet | 0       | Place Colossus Bet Automation | 20.22       | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | SUN-5617   | Auto-Gen | Normal | 0.10   | 99.90   | Colossus    | Sun Jackpots Bet |         | Place Colossus Bet Automation | 0.10        | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | SUN-5617   | Auto-Gen | Free   | 50.00  | 100.00  | Colossus    | Sun Jackpots Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | SUN-5617   | Auto-Gen | Free   | 200.00 | 100.00  | Colossus    | Sun Jackpots Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | SUN-5617   | Auto-Gen | Other  | 50.00  | 50.00   | Colossus    | Sun Jackpots Bet | 5       | Place Colossus Bet Automation | 50.00       | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | Regression | Auto-Gen | Other  | 55.00  | 45.00   | red7        | Tertiary Bet     |         | Regression External Place Bet | 55.00       | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |
      | Regression | Auto-Gen | Free   | 200.00 | 100.00  | red7        | Tertiary Bet     | 1       | Regression External Free Bet  | 0.00        | 0.00         | Accepted    | Cancelled    | Cancel External Bet Id Automation |

  @col-settle
  Scenario Outline: Placing and Settling a Colossus <Class> Bet
    When I place an External bet for $<Stake>
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |
      | bet status      | <PlaceStatus>    |

    And I verify customer statement via API
      | type          | <PlaceType>       |
      | debit amount  | <DebitAmount>     |
      | credit amount | <CreditAmount>    |
      | balance       | <PlaceBetBalance> |
      | description   | <BetDescription>  |

    Then customer balance is equal to $<PlaceBetBalance>

    When I settle an External bet for $<Payout>
      | service name | <ServiceName>  |
      | num settles  | 1              |
      | bet status   | <SettleStatus> |

    And I verify customer statement via API
      | type          | <SettleType>       |
      | debit amount  | 0.00               |
      | credit amount | <Payout>           |
      | balance       | <SettleBetBalance> |
      | description   | <BetDescription>   |

    Then customer balance is equal to $<SettleBetBalance>

    Examples:
      | TestCase | ExtIid   | Class  | Stake  | PlaceType        | SettleType          | DebitAmount | CreditAmount | PlaceBetBalance | SettleBetBalance | ServiceName | BetType | BetDescription                | PlaceStatus | Payout  | SettleStatus       |
      | SUN-5633 | Auto-Gen | Normal | 100.00 | Sun Jackpots Bet | Sun Jackpots Payout | 100.00      | 0.00         | 0.00            | 1025.44          | Colossus    | 0       | Place Colossus Bet Automation | Accepted    | 1025.44 | Settled - Bet Win  |
      | SUN-5633 | Auto-Gen | Normal | 50.00  | Sun Jackpots Bet | Sun Jackpots Payout | 50.00       | 0.00         | 50.00           | 1075.44          | Colossus    |         | Place Colossus Bet Automation | Accepted    | 1025.44 | Settled - Bet Win  |
      | SUN-5633 | Auto-Gen | Free   | 100.00 | Sun Jackpots Bet | Sun Jackpots Payout | 0.00        | 0.00         | 100.00          | 1125.44          | Colossus    | 1       | Place Colossus Free Bet Auto  | Accepted    | 1025.44 | Settled - Bet Win  |
      | SUN-5632 | Auto-Gen | Normal | 100.00 | Sun Jackpots Bet | Sun Jackpots Payout | 100.00      | 0.00         | 0.00            | 1025.44          | Colossus    | 0       | Place Colossus Bet Automation | Accepted    | 0.00    | Settled - Bet Loss |
      | SUN-5632 | Auto-Gen | Normal | 50.00  | Sun Jackpots Bet | Sun Jackpots Payout | 50.00       | 0.00         | 50.00           | 1075.44          | Colossus    |         | Place Colossus Bet Automation | Accepted    | 0.00    | Settled - Bet Loss |
      | SUN-5632 | Auto-Gen | Free   | 100.00 | Sun Jackpots Bet | Sun Jackpots Payout | 0.00        | 0.00         | 100.00          | 1125.44          | Colossus    | 1       | Place Colossus Free Bet Auto  | Accepted    | 0.00    | Settled - Bet Loss |


