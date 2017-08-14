@feed @rabbitmq @redbook
Feature: Event Feeds

  Scenario: PA Feed a Race Event
    When I login in "PA" RabbitMQ and enqueue an Event message based on "feeds/pa-horse-racing.json"
    Then WagerPlayer receives the Event in category "Horse Racing"

  Scenario: WIFT Feed a Race Event
    When I login in "WIFT" RabbitMQ and enqueue an Event message based on "feeds/wift-horse-racing.json"
    Then WagerPlayer receives the Event in category "Horse Racing"

  Scenario: WIFT Feed a Race Event with Scratched runner
    When I login in "WIFT" RabbitMQ and enqueue an Event message based on "feeds/wift-horse-racing-scratched-capton.json"
    Then WagerPlayer receives the Event in category "Horse Racing"
    And The received Event contains scratched selection for "CAPTON"
