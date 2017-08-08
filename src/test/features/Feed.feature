@feed @redbook
Feature: Event Feeds

  Scenario: PA Feed a Race Event
    When I login in "PA" RabbitMQ and enqueue Racing Event message based on "feeds/pa-horse-racing.json"
    Then WagerPlayer receives the "Horse Racing" Event

  Scenario: WIFT Feed a Race Event
    When I login in "WIFT" RabbitMQ and enqueue Racing Event message based on "feeds/wift-horse-racing.json"
    Then WagerPlayer receives the "Horse Racing" Event

  Scenario: WIFT Feed a Race Event with Scratched runner
    When I login in "WIFT" RabbitMQ and enqueue Racing Event message based on "feeds/wift-horse-racing-scratched-capton.json"
    Then WagerPlayer receives the "Horse Racing" Event
    And The received event contains scratched selection for "CAPTON"