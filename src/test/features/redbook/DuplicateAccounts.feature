@redbook @dupaccts

Feature: Verify Duplicate Accounts - UK and EU Customers (SUN-5310 - Duplicate Account Validation SUN-4729)

  @dupacctsnew
  Scenario Outline: <country> Customer Sign-up (Customer Validation)
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street         | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | apimessage                                          |
      | Mr         | SCOTT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Customer Created                                    |
      | Mr         | SCOTT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Customer Created                                    |
      | Mr         | JOE@^&    | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Only letters (Aa-Zz), hyphen and apostrophe allowed |
      | Mr         | LUKAS     | MOCNAY   | 1985-01-01 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | Customer Created                                    |
      | Mr         | LUKAS     | MOCNAY   | 1985-01-01 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | Customer Created                                    |

# Account 01: (SUN-5225) UK Customer sign-up with unique data - Customer Created
# Account 02: (SUN-5226) UK Customer sign-up with matching existing customer data - Error: New Error message
# Account 03: (SUN-5235) UK Customer sign-up with invalid first name - Error: Only letters (Aa-Zz), hyphen and apostrophe allowed
# Account 04: (SUN-5238) EU Customer sign-up with unique data - Customer Created
# Account 05: (SUN-5239) EU Customer sign-up with matching existing customer data - Error: New Error message

  @dupacctsupd
  Scenario Outline: <country> Customer Sign-up, Update customer info, and Sign-up using old credentials
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
    And I update the customer details
      | firstname           | <firstname>             |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | email_address       | <_email_address_suffix> |
      | res_street_address  | <street>                |
      | res_postcode        | <newpostcode>           |
      | res_city            | <city>                  |
      | street              | <street>                |
      | postcode            | <newpostcode>           |
      | city                | <city>                  |
      | country             | <country>               |
      | timezone            | <timezone>              |
      | telephone           | <telephone>             |
      | currency_code       | <currency_code>         |
      | secret_question     | <secret_question>       |
      | deposit_limit       | <deposit_limit>         |
      | client_ip           | <client_ip>             |
      | manual_verification | <manual_verification>   |
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | timezone      | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newlastname | newdob     | newpostcode |
      | Mr         | SCOTT     | MORRISON | 1966-11-11 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | MARLEY      | 1966-11-11 | OX42QX      |
      | Mr         | SCOTT     | LENNON   | 1968-11-06 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | LENNON      | 1971-11-06 | OX42QX      |
      | Mr         | SCOTT     | HENDRIX  | 1969-10-09 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | HENDRIX     | 1969-10-09 | NK23XQ      |

# Account 01: (SUN-5227) UK Customer update Surname and create new customer - Customer Updated and successful new sign-up
# Account 02: (SUN-5228) UK Customer update DOB and create new customer - Customer Updated and successful new sign-up
# Account 03: (SUN-5229) UK Customer update Postcode and create new customer - Customer Updated and successful new sign-up

  @dupacctsdup
  Scenario Outline: <country> Customer Sign-up, Update customer info, and Sign-up using updated credentials
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
    And I update the customer details
      | firstname           | <firstname>             |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | email_address       | <_email_address_suffix> |
      | res_street_address  | <street>                |
      | res_postcode        | <newpostcode>           |
      | res_city            | <city>                  |
      | street              | <street>                |
      | postcode            | <newpostcode>           |
      | city                | <city>                  |
      | country             | <country>               |
      | timezone            | <timezone>              |
      | telephone           | <telephone>             |
      | currency_code       | <currency_code>         |
      | secret_question     | <secret_question>       |
      | deposit_limit       | <deposit_limit>         |
      | client_ip           | <client_ip>             |
      | manual_verification | <manual_verification>   |
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <newlastname>           |
      | dob                   | <newdob>                |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <newpostcode>           |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | timezone      | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newlastname | newdob     | newpostcode |
      | Mr         | SCOTT     | JOPLIN   | 1968-01-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | JETT        | 1968-01-01 | QP44QT      |
      | Mr         | J0%3PH    | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133RP  | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Palacios    | 1980-10-01 | BA133RP     |

# Account 01: (SUN-5230, SUN-5231) UK Customer update customer data and sign-up - Error: New Error message
# Account 02: (SUN-5236) UK Customer update customer data and sign-up - Errors: Only letters (Aa-Zz), hyphen and apostrophe allowed AND New Error message

  @dupacctsamd
  Scenario Outline: Two <country> Customer Sign-up, Update first customer info with the second customer details
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
      | lastname              | <lastname>              |
      | dob                   | <dob>                   |
      | _email_address_suffix | <_email_address_suffix> |
      | telephone             | <telephone>             |
      | building              | <building>              |
      | street                | <street>                |
      | city                  | <city>                  |
      | suburb                | <suburb>                |
      | state                 | <state>                 |
      | postcode              | <postcode>              |
      | country               | <country>               |
      | timezone              | <timezone>              |
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
    And I update the customer details
      | firstname           | <newfirstname>          |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | email_address       | <_email_address_suffix> |
      | res_street_address  | <street>                |
      | res_postcode        | <newpostcode>           |
      | res_city            | <city>                  |
      | street              | <street>                |
      | postcode            | <newpostcode>           |
      | city                | <city>                  |
      | country             | <country>               |
      | timezone            | <timezone>              |
      | telephone           | <telephone>             |
      | currency_code       | <currency_code>         |
      | secret_question     | <secret_question>       |
      | deposit_limit       | <deposit_limit>         |
      | client_ip           | <client_ip>             |
      | manual_verification | <manual_verification>   |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | timezone      | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newfirstname | newlastname | newdob     | newpostcode |
      | Mr         | LUKAS     | MOCNAY   | 1968-01-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | LUKAS        | MOCNAY      | 1968-01-01 | OX42QX      |
      | Mr         | SCOTT     | JOPLIN   | 1980-08-08 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | PP88PP   | N/A   | GB      | Europe/London | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | LUKAS        | MOCNAY      | 1980-01-01 | OX42QX      |

# Account 01, 02: (SUN-5313) Two UK Customers sign-up, update second customer with first customer details - Customers Created and Updated