@feed @redbook @wip
Feature: Event Feed

  Scenario: Create Race Event
    When I login in RabbitMQ and enqueue Racing Event message based on "pa-feed-sample.json"
    Then WagerPlayer will receive the "Horse Racing" Event
