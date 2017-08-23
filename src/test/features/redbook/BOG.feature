@redbook @bog

Feature: Placing and Settling several Single bets on an Event with BOG Enabled
#  Ensure BOG product is enabled at Category Level before running this script

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

# Expected Result: Bets placed at Fixed prices to be paid at SP prices after settlement

  @sp-higher
  Scenario Outline: <ProdName> "<BetType>" Bet Placement for <Category> Event where SP (Win and Place) prices are higher than BOG
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.50, 2.50, 3.50, 4.50, 5.50, 6.50, 7.50, 8.50                                 |
    And I enable "<ProdName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enable "<ProdName2>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |
    And I update "SP" win prices "1.55, 2.55, 3.55, 4.55, 5.55, 6.65, 7.75, 8.85" and place prices "1.13, 1.38, 1.63, 1.88, 2.13, 2.38, 2.63, 2.88"

    When I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner01" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner02" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner03" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner04" for $<Stake>

    Then customer balance after bet is decreased by $<Deduction>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then I verify status and payout of bets placed on runners
      | Runner01 | <Status1> | <Payout1> |
      | Runner02 | <Status2> | <Payout2> |
      | Runner03 | <Status3> | <Payout3> |
      | Runner04 | <Status4> | <Payout4> |
    And customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category     | Subcategory | ProdName | ProdName2 | BetType | Stake | Deduction | TotalPayout | Status1    | Status2     | Status3     | Status4     | Payout1 | Payout2 | Payout3 | Payout4 |
      | Horse Racing | BATH        | BOG      | SP        | Win     | 2.00  | 8.00      | 3.10        | Punter Win | Punter Loss | Punter Loss | Punter Loss | 3.10    | 0.00    | 0.00    | 0.00    |
      | Horse Racing | BATH        | BOG      | SP        | Eachway | 2.00  | 16.00     | 11.38       | Punter Win | Punter Win  | Punter Win  | Punter Loss | 5.36    | 2.76    | 3.26    | 0.00    |

# Expected Result: Bets placed at Fixed prices to be paid at Luxbook DVP prices after settlement

  @bog-higher
  Scenario Outline: <ProdName> "<BetType>" Bet Placement for <Category> Event where BOG (Win and Place) prices are higher than SP
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.50, 2.50, 3.50, 4.50, 5.50, 6.50, 7.50, 8.50                                 |
    And I enable "<ProdName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enable "<ProdName2>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |
    And I update "SP" win prices "1.45, 2.45, 3.45, 4.45, 5.45, 6.45, 7.45, 8.45" and place prices "1.12, 1.37, 1.62, 1.87, 2.12, 2.37, 2.62, 2.87"

    When I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner01" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner02" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner03" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner04" for $<Stake>

    Then customer balance after bet is decreased by $<Deduction>

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then I verify status and payout of bets placed on runners
      | Runner01 | <Status1> | <Payout1> |
      | Runner02 | <Status2> | <Payout2> |
      | Runner03 | <Status3> | <Payout3> |
      | Runner04 | <Status4> | <Payout4> |
    And customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category     | Subcategory | ProdName | ProdName2 | BetType | Stake | Deduction | TotalPayout | Status1    | Status2     | Status3     | Status4     | Payout1 | Payout2 | Payout3 | Payout4 |
      | Horse Racing | BATH        | BOG      | SP        | Win     | 2.00  | 8.00      | 3.00        | Punter Win | Punter Loss | Punter Loss | Punter Loss | 3.00    | 0.00    | 0.00    | 0.00    |
      | Horse Racing | BATH        | BOG      | SP        | Eachway | 2.00  | 16.00     | 11.25       | Punter Win | Punter Win  | Punter Win  | Punter Loss | 5.25    | 2.75    | 3.25    | 0.00    |


  @bog-combo
  Scenario Outline: <ProdName> "<BetType>" Bet Placement for <Category> Event where price is calculated depends on the time of placement
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 1.50, 2.50, 3.50, 4.50, 5.50, 6.50, 7.50, 8.50                                 |
    And I enable "<ProdName>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enable "<ProdName2>" product settings
      | Betting   | Enable Single | Win   |
      | Betting   | Enable Single | EW    |
      | Liability | Display Price | Win   |
      | Liability | Display Price | Place |
    And I enter market details
      | Market Status      | Live           |
      | E/W                | yes            |
      | Bets Allowed       | WIN Yes        |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 3              |

    When I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner01" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner02" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner03" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner04" for $<Stake>

    When I scratch the runners at position(s) "8"

    When I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner01" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner02" for $<Stake>
    And I place a single "<BetType>" bet for "<ProdName>" product on runner "Runner03" for $<Stake>

    Then customer balance after bet is decreased by $<Deduction>

    And I update "SP" win prices "1.47, 2.34, 3.47, 4.47, 5.47, 6.47, 7.47" and place prices "1.12, 1.32, 1.62, 1.87, 2.12, 2.37, 2.62"

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then I verify status and payout of bets placed on runners
      | Runner01 | <Status1> | <Payout1> |
      | Runner02 | <Status2> | <Payout2> |
      | Runner03 | <Status3> | <Payout3> |
      | Runner04 | <Status4> | <Payout4> |
      | Runner01 | <Status5> | <Payout5> |
      | Runner02 | <Status6> | <Payout6> |
      | Runner03 | <Status7> | <Payout7> |
    And customer balance since last bet is increased by $<TotalPayout>

    Examples:
      | Category     | Subcategory | ProdName | ProdName2 | BetType | Stake | Deduction | TotalPayout | Status1    | Status2     | Status3     | Status4     | Payout1 | Payout2 | Payout3 | Payout4 | Status5    | Status6     | Status7     | Payout5 | Payout6 | Payout7 |
      | Horse Racing | BATH        | BOG      | SP        | Win     | 2.00  | 14.00     | 5.94        | Punter Win | Punter Loss | Punter Loss | Punter Loss | 2.94    | 0.00    | 0.00    | 0.00    | Punter Win | Punter Loss | Punter Loss | 3.00    | 0.00    | 0.00    |
      | Horse Racing | BATH        | BOG      | SP        | Eachway | 2.00  | 28.00     | 22.35       | Punter Win | Punter Win  | Punter Win  | Punter Loss | 5.18    | 2.68    | 3.24    | 0.00    | Punter Win | Punter Win  | Punter Win  | 5.25    | 2.75    | 3.25    |
