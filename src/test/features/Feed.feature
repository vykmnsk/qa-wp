@feed @redbook
Feature: Event Feeds

#  Scenario: Create Race Event via PA Feed
#    When I login in "PA" RabbitMQ and enqueue Racing Event message based on "pa-feed-sample.json"
#    Then WagerPlayer will receive the "Horse Racing" Event

  @wip
  Scenario: Create Race Event via WIFT Feed
#    When I login in "WIFT" RabbitMQ and enqueue Racing Event message based on "wift-feed-sample.json"
    Then WagerPlayer receives the "Horse Racing" Event
    And The received event contains scratched selection for "CAPTON"