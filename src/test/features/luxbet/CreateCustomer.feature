@wip @luxbet
Feature: Create Luxbet Customer

  Background: Given I am logged into WP UI and on Home Page

  Scenario Outline: Create Luxbet South Australian Customer on UI
    When I enter specifics to create new customer through wagerplayer
      | title                      | "<title>"                      |
      | firstname                  | "<firstname>"                  |
      | lastname                   | "<lastname>"                   |
      | date_of_birth              | "<date_of_birth>"              |
      | phonenumber                | "<phonenumber>"                |
      | email_address              | "<email_address>"              |
      | address                    | "<address>"                    |
      | country                    | "<country>"                    |
      | weekly_deposit_limit       | "<weekly_deposit_limit>"       |
      | username                   | "<username>"                   |
      | telephone_password         | "<telephone_password>"         |
      | telephone_password_confirm | "<telephone_password_confirm>" |
      | internet_password          | "<internet_password>"          |
      | internet_password_confirm  | "<internet_password_confirm>"  |
      | security_question          | "<security_question>"          |
      | customer_answer            | "<customer_answer>"            |
      | currency                   | "<currency>"                   |
      | timezone                   | "<timezone>"                   |
    Then I see new customer created

    Examples:
      | title | firstname | lastname | date_of_birth | phonenumber | email_address      | address                                              | country   | weekly_deposit_limit | username          | telephone_password | telephone_password_confirm | internet_password   | internet_password_confirm | security_question       | customer_answer   | currency          | timezone         |
      | Mr    | Oswald    | Petrucco | 1939-05-31    | 0421147741  | random@test.com.au | 142 Tynte Street,North Adelaide,South Australia,5006 | Australia | 4500.00              | #RANDOM_USERNAME# | #TELE_PASSWORD#    | #TELE_PASSWORD#            | #INTERNET_PASSWORD# | #INTERNET_PASSWORD#       | Favourite Holiday Spot? | #CHALLENGEANSWER# | Australian Dollar | Australia/Sydney |

  Scenario Outline: Create Luxbet Non South Australian Customer on UI
    When I enter specifics to create new customer through wagerplayer
      | title                      | "<title>"                      |
      | firstname                  | "<firstname>"                  |
      | lastname                   | "<lastname>"                   |
      | date_of_birth              | "<date_of_birth>"              |
      | phonenumber                | "<phonenumber>"                |
      | email_address              | "<email_address>"              |
      | address                    | "<address>"                    |
      | country                    | "<country>"                    |
      | username                   | "<username>"                   |
      | telephone_password         | "<telephone_password>"         |
      | telephone_password_confirm | "<telephone_password_confirm>" |
      | internet_password          | "<internet_password>"          |
      | internet_password_confirm  | "<internet_password_confirm>"  |
      | security_question          | "<security_question>"          |
      | customer_answer            | "<customer_answer>"            |
      | currency                   | "<currency>"                   |
      | timezone                   | "<timezone>"                   |
    Then I see new customer created

    Examples:
      | title | firstname | lastname | date_of_birth | phonenumber | email_address      | address                                    | country   | username          | telephone_password | telephone_password_confirm | internet_password   | internet_password_confirm | security_question       | customer_answer   | currency          | timezone         |
      | Ms    | Keli      | Cowey    | 1952-03-25    | 0463254781  | random@test.com.au | 13a Alan Ave,SEAFORTH,New South Wales,2092 | Australia | #RANDOM_USERNAME# | #TELE_PASSWORD#    | #TELE_PASSWORD#            | #INTERNET_PASSWORD# | #INTERNET_PASSWORD#       | Favourite Holiday Spot? | #CHALLENGEANSWER# | Australian Dollar | Australia/Sydney |

