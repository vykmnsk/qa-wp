@feed @rabbitmq @redbook
Feature: Event Feeds


  Scenario Outline: <type> Feed: Create <category> Event
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in category "<category>"
    Examples:
      | type | category         | template                 |
      | PA   | Horse Racing     | pa-horse-racing.json     |
      | WIFT | Horse Racing     | wift-horse-racing.json   |
      | PA   | Greyhound Racing | pa-greyhound-racing.json |

  Scenario Outline: <type> Feed: Create <category> Event with a Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template>"
    Then WagerPlayer receives the Event in category "<category>"
    And The received Event contains scratched selection for "<selection>"
    Examples:
      | type | category         | template                                    | selection |
      | WIFT | Horse Racing     | wift-horse-racing-scratched-capton.json     | Capton    |
      | PA   | Greyhound Racing | pa-greyhound-racing-scratched-guinness.json | Guinness  |

  Scenario Outline: <type> Feed: Create <category> Event then Update with Scratched selection
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template1>"
    Then WagerPlayer receives the Event in category "<category>"
    When I feed "<type>" RabbitMQ with Event message based on "feeds/<template2>"
    Then WagerPlayer receives the Event in category "<category>"
    And The received Event contains scratched selection for "<selection>"
    Examples:
      | type | category         | template1                | template2                                   | selection |
      | WIFT | Horse Racing     | wift-horse-racing.json   | wift-horse-racing-scratched-capton.json     | Capton    |
      | PA   | Greyhound Racing | pa-greyhound-racing.json | pa-greyhound-racing-scratched-guinness.json | Guinness  |


