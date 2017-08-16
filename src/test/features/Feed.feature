@redbook @feed
Feature: Event Feeds

  @smoke
  Scenario Outline: <type> Feed: Create <category> Event
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    @pa-feed
    Examples:
      | type | category         | subcategory | template             |
      | PA   | Horse Racing     | CHESTER     | pa-hr-chester.json   |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json      |
    @wift-feed
    Examples:
      | type | category         | subcategory | template             |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-bendigo.json |

  @scratched
  Scenario Outline: <type> Feed: Create <category> Event with a Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<selection>"
    @pa-feed
    Examples:
      | type | category         | subcategory | template                              | selection |
      | PA   | Horse Racing     | CHESTER     | pa-hr-chester-scratched-daschas.json  | Daschas   |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove-scratched-guinness.json    | Guinness  |
    @wift-feed
    Examples:
      | type | category         | subcategory | template                              | selection |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-bendigo-scratched-capton.json | Capton    |

  @scratched
  Scenario Outline: <type> Feed: Create <category> Event then Update with Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template1>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template2>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<selection>"
    @pa-feed
    Examples:
      | type | category         | subcategory | template1            | template2                             | selection |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json      | pa-gh-hove-scratched-guinness.json    | Guinness  |
    @wift-feed
    Examples:
      | type | category         | subcategory | template1            | template2                             | selection |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-bendigo.json | wift-hr-bendigo-scratched-capton.json | Capton    |


