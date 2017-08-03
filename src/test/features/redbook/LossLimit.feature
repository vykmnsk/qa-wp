@redbook @loss-limit

Feature: Casino Loss Limit

  @loss-limit
  Scenario Outline: Create Customer, Ensure Loss limit is 0 by default
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

    Then the default loss limit has to be 0.0

    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street           | city     | suburb | postcode | state | country | timezone | secret_question         | currency_code | deposit_limit | client_ip | manual_verification |
      | API     | Mr         | Oswald    | Petrucco | 1939-05-31 | @example.com          | 0463254781 | Level5   | 142 Tynte Street | Adelaide | N/A    | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP           | N/A           | N/A       | Y                   |
