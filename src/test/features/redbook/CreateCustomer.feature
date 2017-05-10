@redbook @create-customer

Feature: Create New Customer

  @credit-card
  Scenario Outline: Create Customer, add Credit Card, make a deposit
    When I create a new customer via <UIorAPI> with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
    Then the customer AML status in <UIorAPI> is updated to <amlStatus>

    When I add credit card to customer and I make a deposit of $<depositAmount>
      | CardNumber     | 5577000055770004 |
      | Cvc            | 737              |
      | ExpiryMonth    | 10               |
      | ExpiryYear     | 2020             |
      | CardHolderName | Gary Milburn     |
      | CardType       | MC               |

    Then customer balance is equal to $<depositAmount>
    When I withdraw $<withrawAmount> using stored "mc" card
    Then customer balance is equal to $<finalBalance>

  @api
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street           | city     | suburb | postcode | state | country | timezone | secret_question         | currency_code | deposit_limit | client_ip | manual_verification | amlStatus             | depositAmount | withrawAmount | finalBalance |
      | API     | Mr         | Oswald    | Petrucco | 1939-05-31 | @example.com          | 0463254781 | Level5   | 142 Tynte Street | Adelaide | N/A    | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP           | N/A           | N/A       | Y                   | active_-_kyc_verified | 100.00        | 20.00              | 80.00   |
#      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0421147741 | Level5   | 13a Alan Ave     | SEAFORTH | N/A    | 2092     | N/A   | IRL     | N/A      | Favourite Holiday Spot? | EUR           | N/A           | N/A       | N                   | UNVERIFIED            | 99.00         | 4.00              | 94.01    |

