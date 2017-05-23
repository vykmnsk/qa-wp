@create-customer @cash-deposit
Feature: Create New Customer

  Scenario Outline: Create Customer
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
    And the customer deposits <depositAmount> cash via <UIorAPI>

# TODO remove? WAPI::account_deposit doesnt work in Redbook
#  @redbook @api
#    Examples:
#      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street           | city     | suburb | postcode | state | country | timezone | secret_question         | currency_code | deposit_limit | client_ip | manual_verification | amlStatus             | depositAmount |
#      | API     | Mr         | Oswald    | Petrucco | 1939-05-31 | @example.com          | 0463254781 | Level5   | 142 Tynte Street | Adelaide | N/A    | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP           | N/A           | N/A       | Y                   | active_-_kyc_verified | 10000.00      |
#      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0421147741 | Level5   | 13a Alan Ave     | SEAFORTH | N/A    | 2092     | N/A   | IRL     | N/A      | Favourite Holiday Spot? | EUR           | N/A           | N/A       | N                   | UNVERIFIED            | 10000.00      |

  @luxbet @api
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street           | city | suburb         | postcode | state | country | timezone         | secret_question         | currency_code | deposit_limit | client_ip   | manual_verification | amlStatus        | depositAmount |
      | API     | Mr         | Oswald    | Petrucco | 1939-05-31 | @example.com          | 0421147741 | N/A      | 142 Tynte Street | N/A  | North Adelaide | 5006     | SA    | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD           | 4500.00       | 61.9.192.13 | N/A                 | account_verified | 10000.00      |
      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0463254781 | N/A      | 13a Alan Ave     | N/A  | SEAFORTH       | 2092     | NSW   | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD           |               | 61.9.192.13 | N/A                 | account_verified | 10000.00      |


  @redbook @ui
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street       | city     | suburb         | postcode | state | country        | timezone      | secret_question         | currency_code | deposit_limit | client_ip | manual_verification | amlStatus  | depositAmount |
      | UI      | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0421147741 | Level5   | 13a Alan Ave | SEAFORTH | Seaforth North | 2092     | N/A   | United Kingdom | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | N/A       | N/A                 | Unverified | 10000.00      |


  @luxbet @ui
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street           | city     | suburb         | postcode | state           | country   | timezone         | secret_question         | currency_code | deposit_limit | client_ip   | manual_verification | amlStatus        | depositAmount |
      | UI      | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0463254781 | N/A      | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | New South Wales | Australia | Australia/Sydney | Favourite Holiday Spot? | AUD           |               | 61.9.192.13 | N/A                 | Account Verified | 10000.00      |
      | UI      | Mr         | Oswald    | Petrucco | 1939-05-31 | @example.com          | 0421147741 | N/A      | 142 Tynte Street | Adelaide | North Adelaide | 5006     | South Australia | Australia | Australia/Sydney | Favourite Holiday Spot? | AUD           | 4500.00       | 61.9.192.13 | N/A                 | Account Verified | 10000.00      |