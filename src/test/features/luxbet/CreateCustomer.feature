@luxbet @create-customer
Feature: Create New Customer

  @wip
  Scenario Outline: Create New Customer
    When I create a new customer via <UIorAPI> with data
      | title              | <title>              |
      | firstName          | <firstName>          |
      | lastName           | <lastName>           |
      | dateOfBirth        | <dateOfBirth>        |
      | telephoneNo        | <telephoneNo>        |
      | email              | <email>              |
      | street             | <street>             |
      | city               | <city>               |
      | suburb             | <suburb>             |
      | state              | <state>              |
      | postCode           | <postCode>           |
      | country            | <country>            |
      | weeklyDepositLimit | <weeklyDepositLimit> |
      | securityQuestion   | <securityQuestion>   |
      | securityAnswer     | <securityAnswer>     |
      | currency           | <currency>           |
      | timezone           | <timezone>           |
      | clientIP           | <clientIP>           |
    Then the customer AML status in <UIorAPI> is updated to "Account Verified" or "Active - KYC verified"

  @api
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | telephoneNo | email                  | street           | city     | suburb         | state | postCode | country | weeklyDepositLimit | clientIP    | securityQuestion        | securityAnswer | currency | timezone         |
      | API     | Mr    | Oswald    | Petrucco | 1939-05-31  | 0421147741  | #username#@example.com | 142 Tynte Street | Adelaide | North Adelaide | SA    | 5006     | AU      | 4500.00            | 61.9.192.13 | Favourite Holiday Spot? | Australia      | AUD      | Australia/Sydney |
      | API     | Ms    | Keli      | Cowey    | 1952-03-25  | 0463254781  | #username#@example.com | 13a Alan Ave     | SEAFORTH | SEAFORTH       | NSW   | 2092     | AU      |                    | 61.9.192.13 | Favourite Holiday Spot? | Australia      | AUD      | Australia/Sydney |


  @ui
    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | telephoneNo | email                  | street       | city     | suburb   | state           | postCode | country | weeklyDepositLimit | clientIP    | securityQuestion        | securityAnswer | currency          | timezone         |
      | UI      | Ms    | Keli      | Cowey    | 1952-03-25  | 0463254781  | #username#@example.com | 13a Alan Ave | SEAFORTH | SEAFORTH | New South Wales | 2092     | AU      |                    | 61.9.192.13 | Favourite Holiday Spot? | Australia      | Australian Dollar | Australia/Sydney |