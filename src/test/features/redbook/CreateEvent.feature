@redbook
Feature: Create Event

  Background:
    Given I am logged in and on Home Page

  Scenario Outline: Create Horse Racing event
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I see New Event page is loaded
    And I enter event details with <NumberOfRunners> horses, current 'show time' and 'event date/time' in 30 minutes with data
      | event name      | <RaceNumber>. TEST RACE 0<RaceNumber> <RaceType> |
      | bet in run type | Both Allowed                                           |
      | create market   | Racing Live                                            |
    Then I see Create Market page

    When I enter odds
    Then I can see success status with message "Market Created"

    When I update race number to "<RaceNumber>"

#  Luxbook DVP Fixed
#  NSW Daily Double
  
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Display Price | Fluc |

    And I enable "PA SP" product settings
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
      | Market Status      | Held           |
      | Bets Allowed       | WIN No         |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 4              |
      | E/W                | yes            |
    Then I can see success status with message "Market display updated"

    Examples:
      | NumberOfRunners | RaceType | RaceNumber |
      | 24              | HANDICAP | 1          |
      | 15              | Auto     | 2          |
      | 8               | Auto     | 3          |
