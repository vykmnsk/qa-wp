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
      | securityAnswer     | <securityAnswer>     |
      | city               | <city>               |
      | currency           | <currency>           |
      | building           | <building>           |
      | postCode           | <postCode>           |
      | suburb             | <suburb>             |
      | postalCounty       | <postalCounty>       |
      | street             | <street>             |
      | state              | <state>              |
      | weeklyDepositLimit | <weeklyDepositLimit> |
      | timezone           | <timezone>           |
      | clientIP           | <clientIP>           |
      | manualVerification | <manualVerification> |

    Then the customer AML status in <UIorAPI> is updated to <amlStatus>

  @api @redbook
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | street           | city     | suburb         | postCode | country | securityQuestion        | securityAnswer | currency | telephoneNo | building | postalCounty | state | weeklyDepositLimit | timezone | clientIP | manualVerification | amlStatus             |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 142 Tynte Street | Adelaide | North Adelaide | 5006     | GB      | Favourite Holiday Spot? | Australia      | GBP      | 0463254781  | Level5   | Parammatta   | N/A   | N/A                | N/A      | N/A      | Y                  | active_-_kyc_verified |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | IRL     | Favourite Holiday Spot? | Australia      | EUR      | 0421147741  | Level5   | Parammatta   | N/A   | N/A                | N/A      | N/A      | N                  | UNVERIFIED            |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | #username#@example.com | 142 Tynte Street | Adelaide | North Adelaide | 5006     | GB      | Favourite Holiday Spot? | Australia      | GBP      | 0463254781  | Level5   | Parammatta   | N/A   | N/A                | N/A      | N/A      | Y                  | active_-_kyc_verified |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | #username#@example.com | 13a Alan Ave     | SEAFORTH | SEAFORTH       | 2092     | IRL     | Favourite Holiday Spot? | Australia      | EUR      | 0421147741  | Level5   | Parammatta   | N/A   | N/A                | N/A      | N/A      | N                  | UNVERIFIED            |

  @api @luxbet
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | telephoneNo | email                  | street           | city     | suburb         | state | postCode | country | weeklyDepositLimit | clientIP    | securityQuestion        | securityAnswer | currency | timezone         | amlStatus        |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | 0421147741  | #username#@example.com | 142 Tynte Street | Adelaide | North Adelaide | SA    | 5006     | AU      | 4500.00            | 61.9.192.13 | Favourite Holiday Spot? | Australia      | AUD      | Australia/Sydney | account_verified |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | 0463254781  | #username#@example.com | 13a Alan Ave     | SEAFORTH | SEAFORTH       | NSW   | 2092     | AU      |                    | 61.9.192.13 | Favourite Holiday Spot? | Australia      | AUD      | Australia/Sydney | account_verified |

  @ui @luxbet
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | telephoneNo | email                  | street       | city     | suburb   | state           | postCode | country | weeklyDepositLimit | clientIP    | securityQuestion        | securityAnswer | currency          | timezone         | amlStatus        |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | 0463254781  | #username#@example.com | 13a Alan Ave | SEAFORTH | SEAFORTH | New South Wales | 2092     | AU      |                    | 61.9.192.13 | Favourite Holiday Spot? | Australia      | Australian Dollar | Australia/Sydney | Account Verified |