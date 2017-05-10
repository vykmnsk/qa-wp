@luxbet-mobile @create-affiliate-customer
Feature: Create New Affiliate Customer

  Scenario Outline: Create Affiliate Customer
    When I create a new customer via <UIorAPI> with data
      | salutation                 | <salutation>                 |
      | firstname                  | <firstname>                  |
      | lastname                   | <lastname>                   |
      | dob                        | <dob>                        |
      | _email_address_suffix      | <_email_address_suffix>      |
      | telephone                  | <telephone>                  |
      | street                     | <street>                     |
      | city                       | <city>                       |
      | suburb                     | <suburb>                     |
      | state                      | <state>                      |
      | postcode                   | <postcode>                   |
      | country                    | <country>                    |
      | currency_code              | <currency_code>              |
      | secret_question            | <secret_question>            |
      | deposit_limit              | <deposit_limit>              |
      | client_ip                  | <client_ip>                  |
      | unit_number                | <unit_number>                |
      | building_number            | <building_number>            |
      | street_name                | <street_name>                |
      | street_type                | <street_type>                |
      | residential_street_address | <residential_street_address> |
      | residential_suburb         | <residential_suburb>         |
      | currency_code              | <currency_code>              |

    Then the affiliate customer should be able to login to mobile site successfully

    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone   | unit_number | building_number | street_name | street_type | residential_street_address | residential_suburb | city    | suburb    | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip      |
      | API     | Mr         | Berlin    | Test     | 1987-02-09 | @example.com          | 44123123123 |             | 135             | Brompton    | Road        | 2 delamare                 | Albanvale          | Croydon | Albanvale | 3136     | ACT   | AU      | Favourite Holiday Spot? | AUD           | 4500.00       | 202.150.115.76 |
