@ui @luxbet
Feature: Create a Luxbet Event

  Background:
    Given I am logged into WP UI and on Home Page

  Scenario Outline: Create Horse Racing event
    When I enter specifics category "<Category>" and subcategory "<Subcategory>"
    Then I see New Event page is loaded

    When I enter event details with <NumberOfRunners> runners, current 'show time' and 'event date/time' in 30 minutes with data
      | base name       | <RaceNumber>: TEST RACE |
      | bet in run type | Both Allowed            |
      | create market   | Racing Live             |
    Then I see Create Market page

    When I enter random prices matching <NumberOfRunners>
    Then I can see success status with message "Market Created"

    When I update race number to "<RaceNumber>"

    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Display Price | Fluc |

    And I enable "NSW Daily Double" product settings
      | Betting  | Enabled       | On    |
      | Betting  | Enabled       | Auto  |
      | Betting  | Display Price | Win   |
      | Betting  | Display Price | Place |
      | Defaults | Display       | Web   |
      | Defaults | Display       | F2    |

    And I enable "Tatts Exacta" product settings
      | Betting | Enabled       | On    |
      | Betting | Enabled       | Auto  |
      | Betting | Display Price | Win   |
      | Betting | Display Price | Place |

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
      | Category         | Subcategory   | NumberOfRunners | RaceNumber | PlaceFraction | NoOfPlaces |
#      |                  |               | 24              | 1          | 1/4           | 4          |
#      |                  |               | 15              | 2          | 1/5           | 3          |
      | Horse Racing     | WOLVERHAMPTON | 8               | 3          | 1/5           | 3          |
      | GREYHOUND RACING | CORK          | 8               | 3          | 1/5           | 3          |
      | Harness Racing   | ALBION PARK   | 8               | 3          | 1/5           | 3          |


