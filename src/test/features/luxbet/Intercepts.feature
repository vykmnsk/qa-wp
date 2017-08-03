@luxbet @intercepts
Feature: ACCEPT REJECT PARTIAL on Intercept bets

  Background:
    Given A new default customer with $5000.00 balance is created and logged in API
    When I am logged into WP UI and on Home Page
    And I modify Intercept on Bet Placement Racing to "Yes" on customer config for default customer

  Scenario Outline: <interceptAction> Single bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00                                                           |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "<betType>" bet on the runner "<runner>" for $<stake> and "<interceptAction>" the intercept
    Then customer balance is equal to $<balanceBeforeSettlement>

    When I result race with the runners and positions
      | ROCKING HORSE | 1 |
      | COLORADO MISS | 2 |
      | CADEYRN       | 3 |
    And I settle race
    Then customer balance is equal to $<balanceAfterSettlement>

  @smoke
    Examples:
      | interceptAction | betType | runner        | stake  | balanceBeforeSettlement | balanceAfterSettlement |
      | Accept          | Win     | ROCKING HORSE | 400.00 | 4600.00                 | 10600.00               |

    Examples:
      | interceptAction | betType | runner        | stake  | balanceBeforeSettlement | balanceAfterSettlement |
      | Reject          | Win     | ROCKING HORSE | 400.00 | 5000.00                 | 5000.00                |

  Scenario: ACCEPT PARTIAL Single bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "Automation Horse Racing"
    And I create a default Racing event with details
      | runners | ROCKING HORSE, COLORADO MISS, CADEYRN, PROSPECT ROAD, WHITE LADY, SUPERBEE, FIGHT FOR GLORY, BONUS SPIN, TORCHBEARER, TRUST ME |
      | prices  | 15.00, 1.40, 14.00, 13.00, 10.00, 26.00, 735.00, 15.00, 61.00, 23.00                                                           |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 1.10, 2.70, 2.60, 2.25, 3.95, 1.95, 2.85, 7.05, 3.65"

    When I place a single "Win" bet on the runner "ROCKING HORSE" for $400.00 and do Partial the intercept for $20.00
    Then customer balance is equal to $4980.00

    When I result race with the runners and positions
      | ROCKING HORSE | 1 |
      | COLORADO MISS | 2 |
      | CADEYRN       | 3 |
    And I settle race
    Then customer balance is equal to $5280.00

  Scenario: ACCEPT Multi bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Treble Bet "Win"-"Win"-"Win" on the runners "Runner01,Runner11,Runner21" for $400.00 with flexi as "N" and do "Accept" the intercept
    Then customer balance is equal to $4600.00

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is equal to $154600.00

  Scenario: REJECT Multi bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Treble Bet "Win"-"Win"-"Win" on the runners "Runner01,Runner11,Runner21" for $400.00 with flexi as "N" and do "Reject" the intercept
    Then customer balance is equal to $5000.00

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is equal to $5000.00

  Scenario: ACCEPT PARTIAL Multi bet on Intercept
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Treble Bet "Win"-"Win"-"Win" on the runners "Runner01,Runner11,Runner21" for $400.00 with flexi as "N" and do Partial the intercept for $20.00
    Then customer balance is equal to $4980.00

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is equal to $17080.00

  Scenario: ACCEPT ALL Multi PATENT bets on Intercept
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Multi Bet "Patent" on the runners "Runner01,Runner11,Runner21" for $400.00 with flexi as "N" and do "Accept,Accept,Accept,Accept,Accept,Accept,Accept" the intercepts with partial amount as $20.00
    Then customer balance is equal to $2200.00

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is equal to $255400.00

  # JIRA bug exists : WAGDOMCI-2675
  Scenario: ACCEPT REJECT PARTIAL Multi PATENT bets on Intercept
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner01, Runner02, Runner03, Runner04, Runner05, Runner06, Runner07, Runner08 |
      | prices  | 5.00, 6.00, 13.00, 2.40, 19.00, 4.40, 26.00, 11.00                             |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.35, 2.65, 4.80, 1.50, 6.70, 2.15, 9.10, 8.60"
    When I enter specifics category "Horse Racing" and subcategory "SEYMOUR"
    And I create a default Racing event with details
      | runners | Runner11, Runner12, Runner13, Runner14, Runner15, Runner16, Runner17, Runner18 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"
    When I enter specifics category "Horse Racing" and subcategory "PAKENHAM"
    And I create a default Racing event with details
      | runners | Runner21, Runner22, Runner23, Runner24, Runner25, Runner26, Runner27, Runner28 |
      | prices  | 11.00, 6.00, 6.50, 8.50, 3.20, 7.00, 6.00, 15.00                               |
    And I enable "Luxbook DVP Fixed" product settings
      | Betting | Enable Single | Win   |
      | Betting | Enable Single | Place |
      | Betting | Enable Single | EW    |
      | Betting | Enable Multi  | Win   |
      | Betting | Enable Multi  | Place |
      | Betting | Enable Multi  | EW    |
    And I enter market details
      | Market Status      | Live      |
      | Bets Allowed       | WIN Yes   |
      | Bets Allowed Place | PLACE DVP |
      | Place Fraction     | -         |
      | No of Places       | 3         |
      | E/W                | yes       |
    And I update fixed place prices "2.85, 2.00, 2.05, 2.40, 1.45, 2.15, 2.00, 3.55"

    When I place a Luxbet Multi Bet "Patent" on the runners "Runner01,Runner11,Runner21" for $400.00 with flexi as "N" and do "Accept,Reject,Partial,Accept,Accept,Accept,Accept" the intercepts with partial amount as $20.00
    Then customer balance is equal to $2980.00

    When I result/settle created event race with winners "Runner01,Runner02,Runner03"
    When I result/settle created event race with winners "Runner11,Runner12,Runner13"
    When I result/settle created event race with winners "Runner21,Runner22,Runner23"
    Then customer balance is equal to $186880.00