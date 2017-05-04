@redbook @create-customer @credit-card @api

Feature: Create New Customer

  Scenario Outline: Create Customer
    When I create a new customer via API with data
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
      | street             | <street>             |
      | state              | <state>              |
      | weeklyDepositLimit | <weeklyDepositLimit> |
      | timezone           | <timezone>           |
      | clientIP           | <clientIP>           |
      | manualVerification | <manualVerification> |
    Then the customer AML status in <UIorAPI> is updated to <amlStatus>

    When I add credit card to customer and I make a deposit of $99.00
      | CardNumber     | 5577000055770004 |
      | Cvc            | 737              |
      | ExpiryMonth    | 10               |
      | ExpiryYear     | 2020             |
      | CardHolderName | Gary Milburn     |
      | CardType       | MC               |
    Then customer balance is at least $99.00
    When I withdraw $20.00 using stored "mc" card
    Then customer balance is decreased by $20.00

    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | street           | city     | suburb         | postCode | country | securityQuestion        | securityAnswer | currency | telephoneNo | building | state | weeklyDepositLimit | timezone | clientIP | manualVerification | amlStatus             |
      | API     | Mr    | Gary      | Milburn  | 1939-05-31  | #username#@example.com | 142 Tynte Street | Adelaide | North Adelaide | 5006     | GB      | Favourite Holiday Spot? | Australia      | GBP      | 0463254781  | Level5   | N/A   | N/A                | N/A      | N/A      | Y                  | active_-_kyc_verified |