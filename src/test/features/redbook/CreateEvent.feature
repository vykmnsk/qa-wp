@ui @redbook
Feature: Create Redbook Event

  Background:
    Given I am logged into WP UI and on Home Page

  Scenario Outline: Create Horse Racing event
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    Then I see New Event page is loaded

    When I enter event details with <NumberOfRunners> runners, current 'show time' and 'event date/time' in 30 minutes with data
      | base name       | <RaceNumber>: TEST RACE |
      | bet in run type | Both Allowed            |
      | create market   | Racing Live             |
    Then I see Create Market page
    And I can see success status with message "Event Created"

    When I enter random prices matching <NumberOfRunners>
    Then I can see success status with message "Market Created"

    When I update race number to "<RaceNumber>"

    And I enable "Luxbook DVP Fixed" product settings
      | Betting   | Display Price   | Fluc        |
      | Liability | Display Price   | Win         |
      | Liability | Display Price   | Place       |
      | Liability | Single          | Win         |
      | Liability | Single          | Place       |
      | Liability | Base            | Win         |
      | Liability | Final Leg Multi | Win         |
      | Liability | Final Leg Multi | Place       |
      | Liability | Multi           | Win         |
      | Liability | Multi           | Place       |
      | Liability | Betback         | Win         |
      | Liability | Betback         | Place       |
      | Liability | Cash Out        | Win         |
      | Liability | Cash Out        | Place       |
      | Liability | Cash Out        | Multi Win   |
      | Liability | Cash Out        | Multi Place |

    And I enable "SP" product settings
      | Betting   | Enabled        | On    |
      | Betting   | Enabled        | Auto  |
      | Betting   | Display Price  | Win   |
      | Betting   | Display Price  | Place |
      | Betting   | Enable Single  | Win   |
      | Betting   | Enable Single  | Place |
      | Betting   | Enable Single  | EW    |
      | Betting   | Display Column | Win   |
      | Betting   | Display Column | Place |
      | Betting   | Display Column | EW    |
      | Betting   | Enable Multi   | Win   |
      | Betting   | Enable Multi   | Place |
      | Betting   | Enable Multi   | EW    |
      | Betting   | Disp. Result   | Win   |
      | Betting   | Disp. Result   | Place |
      | Liability | Display Price  | Win   |
      | Liability | Display Price  | Place |
      | Defaults  | Display        | Web   |
      | Defaults  | Display        | F2    |

    And I enable "Forecast Fixed" product settings
      | Defaults | Display | Web |
      | Defaults | Display | F2  |

    And I enter market details
      | Market Status      | Live            |
      | Bets Allowed       | WIN Yes         |
      | Bets Allowed Place | PLACE Fraction  |
      | Place Fraction     | <PlaceFraction> |
      | No of Places       | <NoOfPlaces>    |
      | E/W                | yes             |
    Then I can see success status with message "Market display updated"
    And event status is "L"

    Examples:
      | NumberOfRunners | RaceNumber | PlaceFraction | NoOfPlaces |
#      | 24              | 1          | 1/4           | 4          |
#      | 15              | 2          | 1/5           | 3          |
      | 8               | 3          | 1/5           | 3          |
