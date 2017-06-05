@redbook @dead-heats
Feature: Dead Heats with several single bets

  Background:
    Given A new default customer with $100.00 balance is created and logged in API
    And I am logged into WP UI and on Home Page

  Scenario Outline: Deadheat: <Category> | <Type>
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 4.2, 10.20, 8.20, 6.00, 12.00, 6.10, 7.20, 8.10, 4.78, 8.20                                        |

    And I enable "Luxbook DVP Fixed" product settings
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
      | No of Places       | <NumOfPlaces>  |
    And I update fixed place prices "<PlacePrices>"

    When I place a single "Win" bet on the runner "Runner01" for $3.10
    And I place a single "Win" bet on the runner "Runner02" for $4.00
    When I place a single "Win" bet on the runner "Runner03" for $2.50
    And I place a single "Eachway" bet on the runner "Runner01" for $5.00
    And I place a single "Eachway" bet on the runner "Runner02" for $4.50
    And I place a single "Eachway" bet on the runner "Runner03" for $6.50
    And I place a single "Eachway" bet on the runner "Runner04" for $7.00
    And I place a single "Eachway" bet on the runner "Runner05" for $8.00
    Then customer balance after bet is decreased by $71.60

    When I result race with the runners and positions "<WinnerWithPositions>"
    And I settle race
    Then customer balance since last bet is increased by $<Payout>

  @smoke
    Examples:
      | Type  | Category     | Subcategory   | PlacePrices                  | NumOfPlaces | WinnerWithPositions                            | Payout |
      | THIRD | Horse Racing | WOLVERHAMPTON | 1.80, 3.30, 2.80, 2.25, 3.75 | 3           | 1:Runner01, 2:Runner02, 3:Runner03, 3:Runner04 | 74.85  |

    Examples:
      | Type   | Category         | Subcategory   | PlacePrices                  | NumOfPlaces | WinnerWithPositions                                        | Payout |
      | FIRST  | Horse Racing     | WOLVERHAMPTON | 1.80, 3.30, 2.80, 2.25, 3.75 | 3           | 1:Runner01, 1:Runner02, 3:Runner03                         | 102.41 |
      | SECOND | Horse Racing     | WOLVERHAMPTON | 1.80, 3.30, 2.80, 2.25, 3.75 | 3           | 1:Runner01, 2:Runner02, 2:Runner03                         | 76.07  |
      | SECOND | Greyhound Racing | HARLOW        | 1.80, 3.30, 2.80, 2.25, 3.75 | 3           | 1:Runner01, 2:Runner02, 2:Runner03                         | 76.07  |
      | FOURTH | Horse Racing     | WOLVERHAMPTON | 1.80, 3.30, 2.80, 2.25, 3.75 | 4           | 1:Runner01, 2:Runner02, 3:Runner03, 4:Runner04, 4:Runner05 | 98.95  |
      | FOURTH | Greyhound Racing | HARLOW        | 1.80, 3.30, 2.80, 2.25, 3.75 | 4           | 1:Runner01, 2:Runner02, 3:Runner03, 4:Runner04, 4:Runner05 | 98.95  |
