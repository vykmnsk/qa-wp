@redbook @feed @gearman-feed

Feature: Sporting Solutions Event Feeds

  Scenario Outline: Gearman Feed: event arrives
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    @smoke
    Examples:
      | category | subcategory          | template                             |
      | Football | NPL Women - Victoria | gearman-football-npl-women-vic.json  |
    Examples:
      | category | subcategory          | template                             |
      | Football | French Ligue 2       | gearman-football-french-ligue-2.json |
      | Tennis   | New Haven            | gearman-tennis-new-haven.json        |

  @wip-markets
  Scenario Outline: Gearman Feed: event arrives with markets
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And the received Event contains markets
    Examples:
      | category | subcategory | template |
#      | Football | Premier League    | gearman-football-premier-league.json    |

  @wip-market-data
  Scenario: Gearman Feed: check arrived event data
    And the received Event market "Match Winner" data matches
      | betting status | Enabled            |
      | market status  | Betting in the Run |
