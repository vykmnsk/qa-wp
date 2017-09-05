@redbook @feed
Feature: Event Feeds

  @smoke
  Scenario Outline: <type> Feed: Create <category> Event
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"

  @pa-feed
    Examples:
      | type | category         | subcategory | template           |
      | PA   | Horse Racing     | CHESTER     | pa-hr-chester.json |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json    |
  @wift-feed
    Examples:
      | type | category     | subcategory | template             |
      | WIFT | Horse Racing | BENDIGO     | wift-hr-bendigo.json |

  @scratched
  Scenario Outline: <type> Feed: Create <category> Event with a Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<scratched>"
  @pa-feed
    Examples:
      | type | category         | subcategory | template                             | scratched |
      | PA   | Horse Racing     | CHESTER     | pa-hr-chester-scratched-daschas.json | Daschas   |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove-scratched-guinness.json   | Guinness  |
  @wift-feed
    Examples:
      | type | category     | subcategory | template                              | scratched |
      | WIFT | Horse Racing | BENDIGO     | wift-hr-bendigo-scratched-capton.json | Capton    |

  @scratched
  Scenario Outline: <type> Feed: Create <category> Event then Update with Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template1>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template2>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<selection>"
  @pa-feed
    Examples:
      | type | category         | subcategory | template1       | template2                          | selection |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json | pa-gh-hove-scratched-guinness.json | Guinness  |
  @wift-feed
    Examples:
      | type | category     | subcategory | template1            | template2                             | selection |
      | WIFT | Horse Racing | BENDIGO     | wift-hr-bendigo.json | wift-hr-bendigo-scratched-capton.json | Capton    |

  @replaced
  Scenario Outline: <type> Feed: Create <category> Event with a Scratched and Replaced selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<scratched>"
    And The received Event contains normal selection for "<replacement>" with the same position as scratched
  @pa-feed
    Examples:
      | type | category         | subcategory | template                                    | scratched | replacement |
      | PA   | Greyhound Racing | Newcastle   | pa-gh-newcastle-replaced-mollys-tourbo.json | Mollys    | Tourbo      |
  @wift-feed
    Examples:
      | type | category         | subcategory | template                                      | scratched | replacement |
      | WIFT | Greyhound Racing | Addington   | wift-gh-addington-replaced-mollys-tourbo.json | Mollys    | Tourbo      |


  @gearman
  Scenario Outline: Gearman Feed from Sporting Solutions
    When I feed Gearman with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"

  @smoke
    Examples:
      | category | subcategory          | template                            |
      | Football | NPL Women - Victoria | gearman-football-npl-women-vic.json |

    Examples:
      | category | subcategory    | template                             |
      | Football | Premier League | gearman-football-premier-league.json |



