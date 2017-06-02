@ui @redbook
Feature: Create Sunbets Events in WagerPlayer UI

  Background:
    Given I am logged into WP UI and on Home Page

  Scenario Outline: Create Event: <Category> | <Subcategory> | <NumOfRunners> runners
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    Then I see New Event page is loaded

    When I enter event details with <NumOfRunners> runners, current 'show time' and 'event date/time' in 30 minutes with data
      | base name       | <RaceNumber>: TEST RACE |
      | bet in run type | Both Allowed            |
      | create market   | Racing Live             |
    Then I see Create Market page
    And I can see success status with message "Event Created"

    When I enter random prices matching <NumOfRunners>
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

    And I enable "Tricast SP" product settings
      | Betting  | Enabled       | On    |
      | Betting  | Enabled       | Auto  |
      | Betting  | Display Price | Win   |
      | Betting  | Display Price | Place |
      | Defaults | Display       | Web   |
      | Defaults | Display       | F2    |

    And I enter market details
      | Market Status      | Live            |
      | Bets Allowed       | WIN Yes         |
      | Bets Allowed Place | PLACE Fraction  |
      | Place Fraction     | <PlaceFraction> |
      | No of Places       | <NoOfPlaces>    |
      | E/W                | yes             |
    Then I can see success status with message "Market display updated"
    And event status is "L"

  @smoke
    Examples:
      | Category     | Subcategory   | NumOfRunners | RaceNumber | PlaceFraction | NoOfPlaces |
      | Horse Racing | WOLVERHAMPTON | 8            | 1          | 1/5           | 3          |
    Examples:
      | Category         | Subcategory   | NumOfRunners | RaceNumber | PlaceFraction | NoOfPlaces |
      | Greyhound Racing | HARLOW        | 15           | 2          | 1/5           | 3          |
      | Horse Racing     | WOLVERHAMPTON | 24           | 3          | 1/4           | 4          |
