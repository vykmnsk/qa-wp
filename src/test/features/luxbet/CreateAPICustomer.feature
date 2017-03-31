@wip @ccapi
Feature: Create Luxbet API Customer

  Scenario Outline: Create Luxbet Customer on API
    When I post customer specifics to create new customer
      | title                | <title>                |
      | firstname            | <firstname>            |
      | lastname             | <lastname>             |
      | date_of_birth        | <date_of_birth>        |
      | phonenumber          | <phonenumber>          |
      | email_address        | <email_address>        |
      | address              | <address>              |
      | country              | <country>              |
      | weekly_deposit_limit | <weekly_deposit_limit> |
      | username             | <username>             |
      | telephone_password   | <telephone_password>   |
      | internet_password    | <internet_password>    |
      | security_question    | <security_question>    |
      | customer_answer      | <customer_answer>      |
      | currency             | <currency>             |
      | timezone             | <timezone>             |
#    Then I see new customer created with AML status updated to "Account Verified" or "Active - KYC verified"

    Examples:
      | title | firstname | lastname | date_of_birth | phonenumber | email_address      | address                                 | country | weekly_deposit_limit | username          | telephone_password | internet_password   | security_question       | customer_answer   | currency | timezone         |
      | Mr    | Oswald    | Petrucco | 1939-05-31    | 0421147741  | random@example.com | 142,Tynte Street,North Adelaide,SA,5006 | AU      | 4500.00              | #RANDOM_USERNAME# | #TELE_PASSWORD#    | #INTERNET_PASSWORD# | Favourite Holiday Spot? | #CHALLENGEANSWER# | AUD      | Australia/Sydney |
      | Ms    | Keli      | Cowey    | 1952-03-25    | 0463254781  | random@example.com | 13a,Alan Ave,SEAFORTH,NSW,2092          | AU      |                      | #RANDOM_USERNAME# | #TELE_PASSWORD#    | #INTERNET_PASSWORD# | Favourite Holiday Spot? | #CHALLENGEANSWER# | AUD      | Australia/Sydney |