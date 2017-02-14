@luxbet
Feature: Create Luxbet Event

  Background:
    Given I am logged in and on Home Page

  Scenario Outline: Create Horse Racing event
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    And I see New Event page is loaded
    And I enter event details with <NumberOfRunners> horses, current 'show time' and 'event date/time' in 30 minutes with data
      | event name      | 1. TEST RACE 01 HANDICAP                                                                                                  |
      | bet in run type | Both Allowed                                                                                                              |
      | create market   | Racing Live                                                                                                               |
      | runners         | SNOW SKY, CRITERION, FAME GAME, OUR IVANHOWE, BIG ORANGE, HARTNELL, HOKKO BRAVE, MAX DYNAMITE, RED CADEAUX, TRIP TO PARIS |
    Then I see Create Market page
    Then I can see success status with message "Market Created"

    When I update race number to "7"

    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Display Price | Fluc |

    And I enable "NSW Daily Double" product settings
      | Betting   | Enabled        | On    |
      | Betting   | Enabled        | Auto  |
      | Betting   | Display Price  | Win   |
      | Betting   | Display Price  | Place |
      | Defaults  | Display        | Web   |
      | Defaults  | Display        | F2    |

    #  NSW Daily Double???

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
      | 8               | Auto     | 3          |
