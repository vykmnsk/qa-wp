@redbook @feed @rabbitmq 
Feature: Event Feeds

  @smoke
  Scenario Outline: <type> Feed: Create <category> Event
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    Examples:
      | type | category         | subcategory | template             |
      | PA   | Horse Racing     | CHESTER     | pa-hr-chester.json   |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-bendigo.json |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json      |

  Scenario Outline: <type> Feed: Create <category> Event with a Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<selection>"
    Examples:
      | type | category         | subcategory | template                      | selection |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-scratched-capton.json | Capton    |
      | PA   | Greyhound Racing | HOVE        | pa-gh-scratched-guinness.json | Guinness  |

  Scenario Outline: <type> Feed: Create <category> Event then Update with Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template1>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template2>"
    Then WagerPlayer receives the Event in "<category>"-"<subcategory>"
    And The received Event contains scratched selection for "<selection>"
    Examples:
      | type | category         | subcategory | template1            | template2                     | selection |
      | WIFT | Horse Racing     | BENDIGO     | wift-hr-bendigo.json | wift-hr-scratched-capton.json | Capton    |
      | PA   | Greyhound Racing | HOVE        | pa-gh-hove.json      | pa-gh-scratched-guinness.json | Guinness  |


