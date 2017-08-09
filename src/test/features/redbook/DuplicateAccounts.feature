@redbook @dup-accts

Feature: Duplicate Account Validations - UK and EU Customers (Stories: SUN-4729, SUN-4893)

# Automated test scenarios under JIRA tasks SUN-5310 and SUN-5311

  Background:
    Given I am logged into WP UI and on Home Page
    And I edit the Master Controller Settings
      | Enable Duplicate Account Checking on Customer Sign-Up | Y |

  @dup-accts-new
  Scenario Outline: <country> Customer Sign-up (Successful Customer Validation)
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street         | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification |
      | Mr         | SCOTT     | PATON    | 1972-02-11 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   |
      | Mr         | LUKAS     | MOCNAY   | 1985-01-11 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   |

# Account 01: (SUN-5225) UK Customer sign-up with unique data - Customer Created
# Account 02: (SUN-5238) EU Customer sign-up with unique data - Customer Created

  @dup-accts-dup
  Scenario Outline: <country> Customer Sign-up (Unsuccessful Customer Validation - Duplicate data)
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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

    And I attempt to create a new customer via API with existing data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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
      | api_resp_message      | <api_resp_msg>          |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street         | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | api_resp_msg                    |
      | Mr         | SCOTT     | PATON    | 1961-12-11 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Customer account already exists |
      | Mr         | LUKAS     | MOCNAY   | 1974-11-11 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | Customer account already exists |

# Account 01: (SUN-5226) UK Customer sign-up with matching existing customer data - Customer account already exists
# Account 02: (SUN-5239) EU Customer sign-up with matching existing customer data - Customer account already exists

  @dup-accts-inv
  Scenario Outline: <country> Customer Sign-up (Unsuccessful Customer Validation - Invalid data)
    When I attempt to create a new customer via API with invalid data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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
      | api_resp_message      | <api_resp_msg>          |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | api_resp_msg                                        |
      | Mr         | JOE@^&    | PATON    | 1971-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Only letters (Aa-Zz), hyphen and apostrophe allowed |
      | Mr         | SC@TT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Only letters (Aa-Zz), hyphen and apostrophe allowed |
      | Mr         | LUKaS     | MOCN@y   | 1985-01-01 | @tabauto.com.au       | 016057709  | 116      |             | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | Residential Street address not provided             |

# Account 01: (SUN-5235) UK Customer sign-up with invalid first name - Error: Only letters (Aa-Zz), hyphen and apostrophe allowed
# Account 02: (SUN-5236) UK Customer update customer data and sign-up - Errors: Only letters (Aa-Zz), hyphen and apostrophe allowed
# Account 03: (SUN-5255) EU Customer update customer data and sign-up - Errors: TBC (see JIRA for the expected result)

  @dup-accts-upd
  Scenario Outline: <country> Successful Customer Sign-up, Successful customer update , and Successful Customer Sign-up using old credentials
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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

    And I update the customer details
      | firstname           | <newfirstname>          |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | postcode            | <newpostcode>           |

    When I create a new customer via API with old customer data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street         | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newfirstname | newlastname | newdob     | newpostcode |
      | Mr         | SCOTT     | MORRISON | 1966-11-11 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | OX42QX   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | SCOTT        | MARLEY      | 1966-11-11 | OX42QX      |
      | Mr         | SCOTT     | LENNON   | 1968-11-06 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | OX42QX   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | SCOTT        | LENNON      | 1971-11-06 | OX42QX      |
      | Mr         | SCOTT     | HENDRIX  | 1969-10-09 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | OX42QX   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | SCOTT        | HENDRIX     | 1969-10-09 | NK23XQ      |
      | Mr         | FREDDY    | MERCURY  | 1955-11-11 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | BOBBY        | MERCURY     | 1955-11-11 | 2           |
      | Mr         | DAVID     | BOWIE    | 1958-12-06 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | DAVID        | HASSELHOLF  | 1958-12-06 | 2           |
      | Mr         | ROBERT    | SMITH    | 1965-05-25 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | ROBERT       | SMITH       | 1977-05-25 | 2           |

# Account 01: (SUN-5227) UK Customer update Surname and create new customer - Customer Updated and successful new sign-up
# Account 02: (SUN-5228) UK Customer update DOB and create new customer - Customer Updated and successful new sign-up
# Account 03: (SUN-5229) UK Customer update Postcode and create new customer - Customer Updated and successful new sign-up
# Account 04: (SUN-5250) EU Customer update Firstname and create new customer - Customer Updated and successful new sign-up
# Account 05: (SUN-5251) EU Customer update Surname and create new customer - Customer Updated and successful new sign-up
# Account 06: (SUN-5252) EU Customer update DOB and create new customer - Customer Updated and successful new sign-up

  @dup-accts-dup2
  Scenario Outline: <country> Successful Customer Sign-up, Successful customer update, and Unsuccessful Customer Sign-up using updated credentials (Duplicate)
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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

    And I update the customer details
      | firstname           | <newfirstname>          |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | postcode            | <newpostcode>           |

    When I attempt to create a new customer via API with existing data
      | salutation            | <salutation>            |
      | firstname             | <newfirstname>          |
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
      | currency_code         | <currency_code>         |
      | secret_question       | <secret_question>       |
      | deposit_limit         | <deposit_limit>         |
      | client_ip             | <client_ip>             |
      | manual_verification   | <manual_verification>   |
      | api_resp_message      | <api_resp_msg>          |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street         | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newfirstname | newlastname | newdob     | newpostcode | api_resp_msg                    |
      | Mr         | SCOTT     | JOPLIN   | 1968-01-01 | @example.com          | 0421147741 | 65       | High Street    | Westbury | Westbury | OX42QX   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | SCOTT        | JETT        | 1968-01-01 | QP44QT      | Customer account already exists |
      | Mr         | DEBIE     | HARRY    | 1958-09-01 | @tabauto.com.au       | 016057709  | 116      | Grafton Street | Dublin   | Dublin   | 2        | N/A   | IRL     | Favourite Holiday Spot? | EUR           | N/A           | 212.58.244.18 | 1                   | MEL          | GIBSON      | 1966-09-01 | 2           | Customer account already exists |

# Account 01: (SUN-5230, SUN-5231) UK Customer update customer data and sign-up - Error: Customer account already exists
# Account 02: (SUN-5253, SUN-5254) EU Customer update customer data and sign-up - Error: Customer account already exists

  @dup-accts-amd
  Scenario Outline: Two <country> Successful Customer Sign-up, Update first customer info with the second customer details
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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

    And I update the customer details
      | firstname           | <newfirstname>          |
      | lastname            | <newlastname>           |
      | dob                 | <newdob>                |
      | postcode            | <newpostcode>           |

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newfirstname | newlastname | newdob     | newpostcode |
      | Mr         | LUKAS     | MOCNAY   | 1968-01-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | OX42QX   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | LUKAS        | MOCNAY      | 1968-01-01 | OX42QX      |
      | Mr         | SCOTT     | JOPLIN   | 1980-08-08 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | PP88PP   | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | LUKAS        | MOCNAY      | 1968-01-01 | OX42QX      |

# Account 01, 02: (SUN-5313) Two UK Customers sign-up, update second customer with first customer details - Customers Created and Updated

  @dup-accts-clo
  Scenario Outline: <country> Customer Sign-up (Customer Validation) and manually update AML status to <newamlstatus>
    When I create a new customer via API with data
      | salutation            | <salutation>            |
      | firstname             | <firstname>             |
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

    And I update the customer AML status to "<newamlstatus>"

    Examples:
      | salutation | firstname | lastname | dob        | _email_address_suffix | telephone  | building | street      | city     | suburb   | postcode | state | country | secret_question         | currency_code | deposit_limit | client_ip     | manual_verification | newamlstatus          |
      | Mr         | SCOTT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Suspended             |
      | Mr         | SCOTT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Closed                |
      | Mr         | SCOTT     | PATON    | 1972-02-01 | @example.com          | 0421147741 | 65       | High Street | Westbury | Westbury | BA133BN  | N/A   | GB      | Favourite Holiday Spot? | GBP           | N/A           | 212.58.244.18 | 1                   | Active - KYC verified |

# Account 01: (SUN-5369) UK Customer sign-up with unique data - Customer Created and update the AML Status to Closed