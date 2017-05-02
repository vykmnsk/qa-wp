@luxbet-mobile @create-affiliate-customer
Feature: Create New Affiliate Customer

  Scenario Outline: Create Affiliate Customer
    When I create a new customer via <UIorAPI> with data
      | title                    | <title>                      |
      | firstName                | <firstName>                  |
      | lastName                 | <lastName>                   |
      | dateOfBirth              | <dateOfBirth>                |
      | email                    | <email>                      |
      | country                  | <country>                    |
      | telephoneNo              | <telephoneNo>                |
      | unitNumber               | <unit_number>                |
      | streetName               | <street_name>                |
      | streetType               | <street_type>                |
      | residentialStreetAddress | <residential_street_address> |
      | residentialSuburb        | <residential_suburb>         |
      | streetAddress            | <street_address>             |
      | securityQuestion         | <securityQuestion>           |
      | city                     | <city>                       |
      | currency                 | <currency>                   |
      | building                 | <building_number>            |
      | postCode                 | <postCode>                   |
      | suburb                   | <suburb>                     |
      | state                    | <state>                      |
      | clientIP                 | <clientIP>                   |
      | manualVerification       | <manualVerification>         |

    Then the affiliate customer should be able to login to mobile site successfully

    Examples:
      | UIorAPI | title | firstName | lastName | dateOfBirth | email                  | telephoneNo | unit_number | building_number | street_name | street_type | residential_street_address | residential_suburb | street_address | city    | suburb    | postCode | state | country | securityQuestion        | currency | clientIP       | manualVerification | amlStatus        |
      | API     | Mr    | Berlin    | Test     | 1987-02-09  | #username#@example.com | 44123123123 |             | 135             | Brompton    | Road        | 2 delamare                 | Albanvale          | 2 delamare     | Croydon | Albanvale | 3136     | ACT   | AU      | Favourite Holiday Spot? | AUD      | 202.150.115.76 | Y                  | account_verified |