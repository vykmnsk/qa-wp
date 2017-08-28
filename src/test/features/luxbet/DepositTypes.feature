@luxbet @deposit-types
Feature: Different money deposit types testing

  Background:
    Given I am logged into WP UI and on Home Page

  Scenario Outline:
    Given I create a new customer via <UIorAPI> with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
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
      | postal_street         | <postal_street>         |
      | postal_suburb         | <postal_suburb>         |
      | postal_state          | <postal_state>          |
      | postal_postcode       | <postal_postcode>       |
      | postal_city           | <postal_city>           |
      | postal_country        | <postal_country>        |
    And I update the customer AML status to "Manually Verified"
    And I update the daily deposit limit to $100.00

    When the customer deposits $100.00 as "BankToBank" via API
    And the customer deposits $100.00 as "BankToBank" using BankToBank tab via UI
    Then the error message "Maximum that can be deposited is now AUD $0.00" is received

    When the customer deposits $100.00 as "BonusOnWinnings" via API
    And the customer deposits $100.00 as "DebtWriteOff" via API
    And the customer deposits $100.00 as "EFTDeposit" via API
    And the customer deposits $100.00 as "CashDeposit" via API
    And the customer deposits $100.00 as "PayoutSport" via API
    And the customer deposits $100.00 as "PayoutRacing" via API
    And the customer deposits $100.00 as "PayoutExotics" via API
    And the customer deposits $100.00 as "VoidedSport" via API
    And the customer deposits $100.00 as "VoidedRacing" via API
    And the customer deposits $100.00 as "VoidedExotics" via API
    And the customer deposits $100.00 as "Poli" via API
    Then customer balance is equal to $1200.00

    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | street       | city | suburb   | postcode | state | country | timezone         | secret_question         | currency_code | deposit_limit | client_ip   | manual_verification | amlStatus        | depositAmount | postal_street | postal_suburb | postal_state | postal_postcode | postal_city | postal_country |
      | API     | Ms         | Test      | Auto     | 1952-03-25 | @example.com          | 0463254781 | 13a Alan Ave | N/A  | SEAFORTH | 2092     | NSW   | AU      | Australia/Sydney | Favourite Holiday Spot? | AUD           |               | 61.9.192.13 | N/A                 | account_verified | 10000.00      |               |               |              |                 |             |                |
