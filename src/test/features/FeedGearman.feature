@feed @gearman @redbook
Feature: Event Feeds via Gearman

  @wip
  Scenario: Gearman Feed from Sporting Solutions
    When I login in Gearman and schedule a job with data based on "feeds/gearman-tennis.json"
    Then WagerPlayer receives the Event in category "Tennis"
