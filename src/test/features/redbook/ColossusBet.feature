@redbook @colossus

Feature: Testing External Bets for Colossus

  Background:
    Given A new default customer with $100.00 balance is created and logged in API

  @col-place
  Scenario Outline: Placing a <ServiceName> <Class> Bet
    When I place an External bet for $<Stake> with status of "Accepted"
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |

    And I verify customer statement via API
      | service name  | <ServiceName>    |
      | type          | <Type>           |
      | debit amount  | <DebitAmount>    |
      | credit amount | <CreditAmount>   |
      | balance       | <Balance>        |
      | description   | <BetDescription> |

    Then customer balance is equal to $<Balance>

    Examples:
      | TestCase           | Class  | Stake  | Balance | ServiceName | Type            | BetType | BetDescription                | DebitAmount | CreditAmount |
      | SUN-5561, SUN-5594 | Normal | 100.00 | 0.00    | Colossus    | The Sun Six Bet | 0       | Place Colossus Bet Automation | 100.00      | 0.00         |
      | SUN-5561, SUN-5594 | Normal | 20.22  | 79.78   | Colossus    | The Sun Six Bet | 0       | Place Colossus Bet Automation | 20.22       | 0.00         |
      | SUN-5561, SUN-5594 | Normal | 0.10   | 99.90   | Colossus    | The Sun Six Bet |         | Place Colossus Bet Automation | 0.10        | 0.00         |
      | SUN-5670           | Free   | 50.00  | 100.00  | Colossus    | The Sun Six Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         |
      | SUN-6147           | Free   | 200.00 | 100.00  | Colossus    | The Sun Six Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | 0.00         |
      |                    | Other  | 50.00  | 50.00   | Colossus    | The Sun Six Bet | 5       | Place Colossus Bet Automation | 50.00       | 0.00         |
      | Regression         | Other  | 55.00  | 45.00   | red7        | Tertiary Bet    |         | Regression External Place Bet | 55.00       | 0.00         |
      | Regression         | Free   | 200.00 | 100.00  | red7        | Tertiary Bet    | 1       | Regression External Free Bet  | 0.00        | 0.00         |

# Note: Ext_id field in API request is auto-generated

  @col-cancel
  Scenario Outline: Refunding a <ServiceName> <Class> Bet
    When I place an External bet for $<Stake> with status of "Accepted"
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |

    And I verify customer statement via API
      | service name  | <ServiceName>    |
      | type          | <Type>           |
      | debit amount  | <DebitAmount>    |
      | credit amount | 0.00             |
      | balance       | <Balance>        |
      | description   | <BetDescription> |

    Then customer balance is equal to $<Balance>

    When I cancel an External bet with status of "Cancelled"
      | service name | <ServiceName> |
      | cancel note  | <CancelNote>  |

    Then customer balance is equal to $100.00

    Examples:
      | TestCase   | Class  | Stake  | Balance | ServiceName | Type            | BetType | BetDescription                | DebitAmount | CancelNote                        |
      | SUN-5617   | Normal | 100.00 | 0.00    | Colossus    | The Sun Six Bet | 0       | Place Colossus Bet Automation | 100.00      | Cancel External Bet Id Automation |
      | SUN-5617   | Normal | 20.22  | 79.78   | Colossus    | The Sun Six Bet | 0       | Place Colossus Bet Automation | 20.22       | Cancel External Bet Id Automation |
      | SUN-5617   | Normal | 0.10   | 99.90   | Colossus    | The Sun Six Bet |         | Place Colossus Bet Automation | 0.10        | Cancel External Bet Id Automation |
      | SUN-5617   | Free   | 50.00  | 100.00  | Colossus    | The Sun Six Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | Cancel External Bet Id Automation |
      | SUN-5617   | Free   | 200.00 | 100.00  | Colossus    | The Sun Six Bet | 1       | Place Colossus Free Bet Auto  | 0.00        | Cancel External Bet Id Automation |
      | SUN-5617   | Other  | 50.00  | 50.00   | Colossus    | The Sun Six Bet | 5       | Place Colossus Bet Automation | 50.00       | Cancel External Bet Id Automation |
      | Regression | Other  | 55.00  | 45.00   | red7        | Tertiary Bet    |         | Regression External Place Bet | 55.00       | Cancel External Bet Id Automation |
      | Regression | Free   | 200.00 | 100.00  | red7        | Tertiary Bet    | 1       | Regression External Free Bet  | 0.00        | Cancel External Bet Id Automation |

  @col-settle
  Scenario Outline: Placing and Settling a <ServiceName> <Class> Bet
    When I place an External bet for $<Stake> with status of "Accepted"
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |

    And I verify customer statement via API
      | type          | <PlaceType>       |
      | debit amount  | <DebitAmount>     |
      | credit amount | 0.00              |
      | balance       | <PlaceBetBalance> |
      | description   | <BetDescription>  |

    Then customer balance is equal to $<PlaceBetBalance>

    When I settle an External bet for $<Payout>
      | service name | <ServiceName>  |
      | num settles  | 1              |
      | bet status   | <SettleStatus> |

    And I verify customer statement via API
      | type          | <SettleType>       |
      | debit amount  | <SettleDebitAmt>   |
      | credit amount | <Payout>           |
      | balance       | <SettleBetBalance> |
      | description   | <BetDescription>   |

    Then customer balance is equal to $<SettleBetBalance>

    Examples:
      | TestCase | Class  | Stake  | PlaceType       | SettleType         | DebitAmount | SettleDebitAmt | PlaceBetBalance | SettleBetBalance | ServiceName | BetType | BetDescription                | Payout  | SettleStatus       |
      | SUN-5633 | Normal | 100.00 | The Sun Six Bet | The Sun Six Payout | 100.00      | 0.00           | 0.00            | 1025.44          | Colossus    | 0       | Place Colossus Bet Automation | 1025.44 | Settled - Bet Win  |
      | SUN-5633 | Normal | 50.00  | The Sun Six Bet | The Sun Six Payout | 50.00       | 0.00           | 50.00           | 1075.44          | Colossus    |         | Place Colossus Bet Automation | 1025.44 | Settled - Bet Win  |
      | SUN-5633 | Free   | 100.00 | The Sun Six Bet | The Sun Six Payout | 0.00        | 0.00           | 100.00          | 1125.44          | Colossus    | 1       | Place Colossus Free Bet Auto  | 1025.44 | Settled - Bet Win  |
      | SUN-5632 | Normal | 100.00 | The Sun Six Bet | The Sun Six Bet    | 100.00      | 100.00         | 0.00            | 0.00             | Colossus    | 0       | Place Colossus Bet Automation | 0.00    | Settled - Bet Loss |
      | SUN-5632 | Normal | 50.00  | The Sun Six Bet | The Sun Six Bet    | 50.00       | 50.00          | 50.00           | 50.00            | Colossus    |         | Place Colossus Bet Automation | 0.00    | Settled - Bet Loss |
      | SUN-5632 | Free   | 100.00 | The Sun Six Bet | The Sun Six Bet    | 0.00        | 0.00           | 100.00          | 100.00           | Colossus    | 1       | Place Colossus Free Bet Auto  | 0.00    | Settled - Bet Loss |


  @col-cashout
  Scenario Outline: Placing and Cashing Out a <ServiceName> <Class> Bet
    When I place an External bet for $<Stake> with status of "Accepted"
      | service name    | <ServiceName>    |
      | bet description | <BetDescription> |
      | bet type        | <BetType>        |

    And I verify customer statement via API
      | type          | <PlaceType>       |
      | debit amount  | <DebitAmount>     |
      | credit amount | 0.00              |
      | balance       | <PlaceBetBalance> |
      | description   | <BetDescription>  |

    Then customer balance is equal to $<PlaceBetBalance>

    When I cashout an External bet for $<Cashout> with status of "Cashed Out"
      | service name | <ServiceName> |

    And I verify customer statement via API
      | type          | <CashoutType>       |
      | debit amount  | 0.00                |
      | credit amount | <Cashout>           |
      | balance       | <CashoutBetBalance> |
      | description   | <BetDescription>    |

    Then customer balance is equal to $<CashoutBetBalance>

    Examples:
      | TestCase | Class  | Stake  | PlaceType       | CashoutType          | DebitAmount | PlaceBetBalance | CashoutBetBalance | ServiceName | BetType | BetDescription                | Cashout |
      | SUN-5633 | Normal | 100.00 | The Sun Six Bet | The Sun Six Cash Out | 100.00      | 0.00            | 1025.44           | Colossus    | 0       | Place Colossus Bet Automation | 1025.44 |
      | SUN-5633 | Normal | 50.00  | The Sun Six Bet | The Sun Six Cash Out | 50.00       | 50.00           | 1075.44           | Colossus    |         | Place Colossus Bet Automation | 1025.44 |
      | SUN-5633 | Free   | 100.00 | The Sun Six Bet | The Sun Six Cash Out | 0.00        | 100.00          | 1125.44           | Colossus    | 1       | Place Colossus Free Bet Auto  | 1025.44 |
      | SUN-5632 | Normal | 100.00 | The Sun Six Bet | The Sun Six Cash Out | 100.00      | 0.00            | 0.01              | Colossus    | 0       | Place Colossus Bet Automation | 0.01    |
      | SUN-5632 | Normal | 50.00  | The Sun Six Bet | The Sun Six Cash Out | 50.00       | 50.00           | 50.01             | Colossus    |         | Place Colossus Bet Automation | 0.01    |
      | SUN-5632 | Free   | 100.00 | The Sun Six Bet | The Sun Six Cash Out | 0.00        | 100.00          | 100.01            | Colossus    | 1       | Place Colossus Free Bet Auto  | 0.01    |

