@luxbet @bonus-bets
Feature: Bonus Bets

  Background:
    Given I am logged into WP UI and on Home Page

  Scenario Outline: Place Bonus Bet with or without Bonus Wallet
    When I create a new customer via <UIorAPI> with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | promo_code            | <promo_code>            |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
    Then the customer AML status in <UIorAPI> is updated to <amlStatus>
    And the customer deposits $<depositAmount> as "CashDeposit" via <UIorAPI>

    When I activate promotion code "SIGNUP101 - Sign Up - AUD $100.00" for default customer in WP with activation note "Bonus bet Promo Code"
    Then customer bonus bet balance is equal to $100.00

    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 1.12, 1.40, 14.00, 13.00, 10.00, 26.00, 35.00, 15.00, 61.00, 23.00                                                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | Place |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
      | Liability | Single        | Win   |
      | Liability | Single        | Place |
      | Liability | Base          | Win   |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | 1/4       |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a bonus single "Place" bet on the runner "ROCKING HORSE" for $<stake> with bonus wallet as "<bonusWallet>"
    Then customer balance is equal to $<depositAmount>
    And customer bonus bet balance is equal to $<bonusBetBalance>
    When I result/settle created event race with winners "<Winners>"
    Then customer balance is equal to $<custBalance>

  @luxbet @smoke
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street       | city | suburb   | postcode | state | country | timezone         | promo_code | secret_question         | currency_code | client_ip   | manual_verification | amlStatus        | depositAmount | stake | Winners                             | bonusWallet | bonusBetBalance | custBalance |
      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0463254781 | N/A      | 13a Alan Ave | N/A  | SEAFORTH | 2092     | NSW   | AU      | Australia/Sydney | bbauto     | Favourite Holiday Spot? | AUD           | 61.9.192.13 | N/A                 | account_verified | 1000.00       | 10.00 | ROCKING HORSE,WHITE LADY,BONUS SPIN | N           | 0.00            | 1185.00     |

  @luxbet
    Examples:
      | UIorAPI | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street       | city | suburb   | postcode | state | country | timezone         | promo_code | secret_question         | currency_code | client_ip   | manual_verification | amlStatus        | depositAmount | stake | Winners                             | bonusWallet | bonusBetBalance | custBalance |
      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0463254781 | N/A      | 13a Alan Ave | N/A  | SEAFORTH | 2092     | NSW   | AU      | Australia/Sydney | bbauto     | Favourite Holiday Spot? | AUD           | 61.9.192.13 | N/A                 | account_verified | 1000.00       | 10.00 | ROCKING HORSE,WHITE LADY,BONUS SPIN | Y           | 90.00           | 1018.50     |
#      | API     | Ms         | Keli      | Cowey    | 1952-03-25 | @example.com          | 0463254781 | N/A      | 13a Alan Ave | N/A  | SEAFORTH | 2092     | NSW   | AU      | Australia/Sydney | bbauto     | Favourite Holiday Spot? | AUD           | 61.9.192.13 | N/A                 | account_verified | 1000.00       | 0.00  | ROCKING HORSE,WHITE LADY,BONUS SPIN | N           | 90.00           | 1018.50     | // Commented for defect WAGDOMCI-2235