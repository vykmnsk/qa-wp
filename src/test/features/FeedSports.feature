@redbook @feed @gearman-feed

Feature: Sporting Solutions Event Feeds

  Background:
    Given A new default customer with $0.00 balance is created and logged in API

  Scenario Outline: Gearman Feed: event arrives
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
  @smoke
    Examples:
      | category | subcategory          | template                            |
      | Football | NPL Women - Victoria | gearman-football-npl-women-vic.json |
    Examples:
      | category | subcategory    | template                             |
      | Football | French Ligue 2 | gearman-football-french-ligue-2.json |
      | Tennis   | New Haven      | gearman-tennis-new-haven.json        |

  @wgearman-feed-markets
  Scenario Outline: Gearman Feed: event arrives with markets
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And the received Event contains markets
    Examples:
      | category | subcategory    | template                             |
      | Football | Premier League | gearman-football-premier-league.json |

  @gearman-feed-market-data
  Scenario Outline: Gearman Feed: check arrived event data
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And the received Event market "Match Winner" data matches
      | betting status | Enabled            |
      | market status  | Betting in the Run |
    Examples:
      | category | subcategory    | template                             |
      | Football | Premier League | gearman-football-premier-league.json |
