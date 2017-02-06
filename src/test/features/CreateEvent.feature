Feature: Create Event

  Background:
    Given I am logged in and on Home Page

  Scenario: Create Horse Racing event
    When I enter specifics category "Horse Racing" and subcategory "WOLVERHAMPTON"
    Then I see New Event page

    When I enter event details with current 'show time' and 'event date/time' in 30 minutes with data
      | event name      | 1. TEST RACE 01 HANDICAP |
      | bet in run type | Both Allowed             |
      | create market   | Racing Live              |
    Then I see Create Market page

#    And I enter runner names
#      | SNOW SKY      |
#      | CRITERION     |
#      | FAME GAME     |
#      | OUR IVANHOWE  |
#      | BIG ORANGE    |
#      | HARTNELL      |
#      | HOKKO BRAVE   |
#      | MAX DYNAMITE  |
#      | RED CADEAUX   |
#      | TRIP TO PARIS |
#    Then I see ??? page
#
#    When I enter odds
#      | position | runner_name   | price |
#      | 1        | SNOW SKY      | 3     |
#      | 2        | CRITERION     | 3     |
#      | 3        | FAME GAME     | 3.5   |
#      | 4        | OUR IVANHOWE  | 2.6   |
#      | 5        | BIG ORANGE    | 5.1   |
#      | 6        | HARTNELL      | 3.5   |
#      | 7        | HOKKO BRAVE   | 3.5   |
#      | 8        | MAX DYNAMITE  | 1.5   |
#      | 9        | RED CADEAUX   | 2.6   |
#      | 10       | TRIP TO PARIS | 5     |
#    Then I see Market Details section
#
#    When I enter market details
#      | raceNum | marketStatus | betAllowedWIN | betAllowedPLACE | placeFraction | noOfPlaces | eW     |
#      | 1       | Live         | WIN Yes       | PLACE Fraction  | 1/4           | 4          | ticked |