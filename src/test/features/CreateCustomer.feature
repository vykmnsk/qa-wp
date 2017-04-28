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

  @redbook @api
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country | timezone | securityQuestion        | currency | weeklyDepositLimit | clientIP | manualVerification | amlStatus             |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0463254781  | Level5   | 142 Tynte Street | Adelaide | North Adelaide | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP      | N/A                | N/A      | Y                  | active_-_kyc_verified |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0463254781  | Level5   | 142 Tynte Street | Adelaide | North Adelaide | 5006     | N/A   | GB      | N/A      | Favourite Holiday Spot? | GBP      | N/A                | N/A      | Y                  | active_-_kyc_verified |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0421147741  | Level5   | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | N/A   | IRL     | N/A      | Favourite Holiday Spot? | EUR      | N/A                | N/A      | N                  | UNVERIFIED            |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0421147741  | Level5   | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | N/A   | IRL     | N/A      | Favourite Holiday Spot? | EUR      | N/A                | N/A      | N                  | UNVERIFIED            |

  @redbook @ui
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country | timezone           | securityQuestion        | currency | weeklyDepositLimit | clientIP | manualVerification | amlStatus  |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0421147741  | Level5   | 142 Tynte Street | Adelaide | North Adelaide | 2092     | N/A   | AU      | Australia/Adelaide | Favourite Holiday Spot? | UK Pound | N/A                | N/A      | N/A                | Unverified |

  @luxbet @api @lux-ft
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street           | city     | suburb         | postCode | state | country | timezone         | securityQuestion        | currency | weeklyDepositLimit | clientIP    | manualVerification | amlStatus        |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 0421147741  | N/A      | 142 Tynte Street | Adelaide | North Adelaide | 5006     | SA    | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD      | 4500.00            | 61.9.192.13 | N/A                | account_verified |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0463254781  | N/A      | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | NSW   | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD      |                    | 61.9.192.13 | N/A                | account_verified |

  @luxbet @ui @lux-ft
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | building | street       | city     | suburb   | postCode | state           | country   | timezone         | securityQuestion        | currency          | weeklyDepositLimit | clientIP    | manualVerification | amlStatus        |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 0463254781  | N/A      | 13a Alan Ave | SEAFORTH | SEAFORTH | 2092     | New South Wales | Australia | Australia/Sydney | Favourite Holiday Spot? | Australian Dollar |                    | 61.9.192.13 | N/A                | Account Verified |
