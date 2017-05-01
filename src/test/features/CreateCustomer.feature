@create-customer
Feature: Create New Customer

  Scenario Outline: Create Customer
    When I create a new customer via <UIorAPI> with data
      | title              | <title>              |
      | firstName          | <firstName>          |
      | lastName           | <lastName>           |
      | dateOfBirth        | <dateOfBirth>        |
      | email              | <email>              |
      | country            | <country>            |
      | telephoneNo        | <telephoneNo>        |
      | securityQuestion   | <securityQuestion>   |
      | city               | <city>               |
      | currency           | <currency>           |
      | building           | <building>           |
      | postCode           | <postCode>           |
      | suburb             | <suburb>             |
      | street             | <street>             |
      | state              | <state>              |
      | weeklyDepositLimit | <weeklyDepositLimit> |
      | timezone           | <timezone>           |
      | clientIP           | <clientIP>           |
      | manualVerification | <manualVerification> |
    Then the customer AML status in <UIorAPI> is updated to <amlStatus>
    And the customer deposits <depositAmount> cash via <UIorAPI>

  @redbook @api
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country | timezone | securityQuestion        | currency | weeklyDepositLimit | clientIP | manualVerification | amlStatus             | depositAmount |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0463254781  | Level5   | 142 Tynte Street | Adelaide | North Adelaide | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP      | N/A                | N/A      | Y                  | active_-_kyc_verified | 10000.00      |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0421147741  | Level5   | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | N/A   | IRL     | N/A      | Favourite Holiday Spot? | EUR      | N/A                | N/A      | N                  | UNVERIFIED            | 10000.00      |

  @redbook @ui
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country   | timezone           | securityQuestion        | currency | weeklyDepositLimit | clientIP | manualVerification | amlStatus  | depositAmount |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0421147741  | Level5   | 142 Tynte Street | Adelaide | North Adelaide | 2092     | N/A   | Australia | Australia/Adelaide | Favourite Holiday Spot? | GBP      | N/A                | N/A      | N/A                | Unverified | 10000.00      |

  @luxbet @api
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country | timezone         | securityQuestion        | currency | weeklyDepositLimit | clientIP    | manualVerification | amlStatus        | depositAmount |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0421147741  | N/A      | 142 Tynte Street | Adelaide | North Adelaide | 5006     | SA    | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD      | 4500.00            | 61.9.192.13 | N/A                | account_verified | 10000.00      |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0463254781  | N/A      | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | NSW   | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD      |                    | 61.9.192.13 | N/A                | account_verified | 10000.00      |

  @luxbet @ui
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state           | country   | timezone         | securityQuestion        | currency | weeklyDepositLimit | clientIP    | manualVerification | amlStatus        | depositAmount |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0463254781  | N/A      | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | New South Wales | Australia | Australia/Sydney | Favourite Holiday Spot? | AUD      |                    | 61.9.192.13 | N/A                | Account Verified | 10000.00      |
      | UI      | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0421147741  | N/A      | 142 Tynte Street | Adelaide | North Adelaide | 5006     | South Australia | Australia | Australia/Sydney | Favourite Holiday Spot? | AUD      | 4500.00            | 61.9.192.13 | N/A                | Account Verified | 10000.00      |