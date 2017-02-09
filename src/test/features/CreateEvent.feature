Feature: Create Event

  Background:
    Given I am logged in and on Home Page

  Scenario: Create Horse Racing event
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    Then I see New Event page

    When I enter event details with current 'show time' and 'event date/time' in 30 minutes with data
      | event name      | 1. TEST RACE 01 HANDICAP                                                                                                  |
      | bet in run type | Both Allowed                                                                                                              |
      | create market   | Racing Live                                                                                                               |
      | runners         | SNOW SKY, CRITERION, FAME GAME, OUR IVANHOWE, BIG ORANGE, HARTNELL, HOKKO BRAVE, MAX DYNAMITE, RED CADEAUX, TRIP TO PARIS |
    Then I see Create Market page
    When I enter odds "3, 3, 3.5, 2.6, 5.1, 3.5, 3.5, 1.5, 2.6, 5"
    Then I can see success status with message "Market Created"

    When I update race number to "7"
    And I enable "Win / Place" "PA SP" product settings
      | Betting   | Enabled       | On    |
      | Betting   | Enabled       | Auto  |
      | Betting   | Display Price | Win   |
      | Betting   | Display Price | Place |
      | Betting   | Enable Single | Win   |
      | Liability | Display Price | Win   |

    And I enter market details
      | Market Status      | Held           |
      | Bets Allowed       | WIN No         |
      | Bets Allowed Place | PLACE Fraction |
      | Place Fraction     | 1/4            |
      | No of Places       | 4              |
      | E/W                | yes            |
    Then I can see success status with message "Market display updated"
