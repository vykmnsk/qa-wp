@luxbet @single-bets @fixed-single-bets
Feature: Placing and Settling multiple Single bets on a Luxbet event

  Background:
    Given I am logged into WP API
    And I am logged into WP UI and on Home Page

  @lux-ft
  Scenario Outline: Horse Race multiple Single bets
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I create a default event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner 06, Runner07, Runner08, Runner09, Runner10 |
      | prices  | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00                                |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed win prices "<WinPrices>" and place prices "<PlacePrices>"
    And customer balance is at least $20.50

    When I place a single "Win" bet on the runner "Runner01" for $5.50
    And I place a single "Win" bet on the runner "Runner01" for $3.50
    When I place a single "Place" bet on the runner "Runner01" for $2.50
    And I place a single "Eachway" bet on the runner "Runner02" for $5.50
    And I place a single "Eachway" bet on the runner "Runner03" for $2.50
    Then customer balance is decreased by $27.50

    When I result race with the runners and positions
      | Runner01 | 1 |
      | Runner02 | 2 |
      | Runner03 | 3 |
    And I settle race
    Then customer balance is increased by $<Payout>

    Examples:
      | WinPrices                                                            | PlacePrices                                                | Payout |
      | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00 | 2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65 | 23.26  |