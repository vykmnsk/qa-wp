@single-bets
Feature: Single Bets

  Background:
    Given I am logged into WP API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Horse Race Win Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 3.30, 4.40, 5.50                     |
    And I enable "<ProductName>" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

    And customer balance is at least $20.50

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
    And I settle the race with Win prices "<WinPrices>" and Place prices "<PlacePrices>"
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName       | BetType | BetOn    | Stake | BalanceDeductedBy | Payout | WinPrices  | PlacePrices |
      | Luxbook DVP Fixed | Win     | Runner01 | 2.50  | 2.50              | 2.75   | 4.20, 4.10 | 3.90, 1.29  |
      | Luxbook DVP Fixed | Win     | Runner02 | 2.50  | 2.50              | 0.00   | 2.20, 5.10 | 3.90, 1.29  |


  Scenario Outline: Horse Race Eachway or Place Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05 |
      | prices  | 1.10, 2.20, 3.30, 4.40, 5.50                     |
    And I enable "<ProductName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | Place |
      | Betting   | Enable Single | EW    |
      | Betting   | Display Price | Win   |
      | Betting   | Display Price | Place |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |
    And I update fixed win prices "<WinPrices>" and place prices "<PlacePrices>"
    And customer balance is at least $20.50

    When I place a single "<BetType>" bet on the runner "<BetOn>" for $<Stake>
    Then customer balance is decreased by $<BalanceDeductedBy>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance is increased by $<Payout>

    Examples:
      | ProductName       | BetType | BetOn    | Stake | BalanceDeductedBy | Payout | WinPrices  | PlacePrices |
      | Luxbook DVP Fixed | EachWay | Runner01 | 2.50  | 5.00              | 22.75  | 5.20, 2.10 | 3.90, 1.29  |

  @luxbet
    Examples:
      | ProductName       | BetType | BetOn    | Stake | BalanceDeductedBy | Payout | WinPrices  | PlacePrices |
      | Luxbook DVP Fixed | Place   | Runner01 | 2.50  | 2.50              | 9.75   | 3.20, 1.10 | 3.90, 1.29  |
