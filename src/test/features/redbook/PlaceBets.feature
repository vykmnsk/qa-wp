@placebets
Feature: Place Bets

   Scenario: Should successfully place a single win Bets
        Given I am a Sunbets customer
        When I place a single Win bet for $34.66
        Then I should receive a success message