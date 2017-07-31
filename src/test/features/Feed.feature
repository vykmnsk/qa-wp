@feed @redbook
Feature: Event Feed

  Scenario: Create Race Event
    When I login in RabbitMQ and enqueue event message based on "feed-sample.json"
