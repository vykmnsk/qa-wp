@luxbet
Feature: Create Luxbet API Customer

  @api
  Scenario Outline: Create Luxbet Customer using API
    When I post customer specifics to create new customer
      | title                | <title>                |
      | firstname            | <firstname>            |
      | lastname             | <lastname>             |
      | date_of_birth        | <date_of_birth>        |
      | phonenumber          | <phonenumber>          |
      | email_address        | <email_address>        |
      | street_address       | <streetaddress>        |
      | suburb               | <suburb>               |
      | state                | <state>                |
      | postcode             | <postcode>             |
      | country              | <country>              |
      | weekly_deposit_limit | <weekly_deposit_limit> |
      | client_ip            | <clientip>             |
      | security_question    | <security_question>    |
      | customer_answer      | <customer_answer>      |
      | currency             | <currency>             |
      | timezone             | <timezone>             |
    Then I verify a new customer created with AML status "account_verified" or "active_kyc_verified"

    Examples:
      | title | firstname | lastname | date_of_birth | phonenumber | email_address      | streetaddress    | suburb         | state | postcode | country | weekly_deposit_limit | clientip    | security_question       | customer_answer | currency | timezone         |
      | Mr    | Oswald    | Petrucco | 1939-05-31    | 0421147741  | #username#@example.com | 142 Tynte Street | North Adelaide | SA    | 5006     | AU      | 4500.00              | 61.9.192.13 | Favourite Holiday Spot? | Australia       | AUD      | Australia/Sydney |
      | Ms    | Keli      | Cowey    | 1952-03-25    | 0463254781  | #username#@example.com | 13aAlan Ave      | SEAFORTH       | NSW   | 2092     | AU      |                      | 61.9.192.13 | Favourite Holiday Spot? | Australia       | AUD      | Australia/Sydney |


  @ui
  Scenario Outline: Create Luxbet Customer using UI
    Given I am logged into WP UI and on Home Page
    When I navigate to Customers Page to insert new customer
    And I enter the following data on Create New Customer page
      | title                | <title>                |
      | firstname            | <firstname>            |
      | lastname             | <lastname>             |
      | date_of_birth        | <date_of_birth>        |
      | phonenumber          | <phonenumber>          |
      | email_address        | <email_address>        |
      | street_address       | <streetaddress>        |
      | suburb               | <suburb>               |
      | city                 | <city>                 |
      | state                | <state>                |
      | postcode             | <postcode>             |
      | country              | <country>              |
      | weekly_deposit_limit | <weekly_deposit_limit> |
      | security_question    | <security_question>    |
      | customer_answer      | <customer_answer>      |
      | currency             | <currency>             |
      | timezone             | <timezone>             |
    Then I see new customer created with AML status updated to "Account Verified" or "Active - KYC verified"

    Examples:
      | title | firstname | lastname | date_of_birth | phonenumber | email_address          | streetaddress    | suburb         | city          | state           | postcode | country   | weekly_deposit_limit | security_question       | customer_answer | currency          | timezone         |
      | Mr    | Oswald    | Petrucco | 1939-05-31    | 0421147741  | #username#@test.com.au | 142 Tynte Street | North Adelaide | North Adelaid | South Australia | 5006     | Australia | 4500.00              | Favourite Holiday Spot? | Australia       | Australian Dollar | Australia/Sydney |
      | Ms    | Keli      | Cowey    | 1952-03-25    | 0463254781  | #username#@test.com.au | 13a Alan Ave     | SEAFORTH       | SEAFORTH      | New South Wales | 2092     | Australia |                      | Favourite Holiday Spot? | Australia       | Australian Dollar | Australia/Sydney |
