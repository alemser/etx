Feature: Handle coordination requests

  Scenario: Create new coordination
    Given the following payload
      | {"business_key": "myBK"} |
    When I POST to the coordination endpoint
    Then the response status code is 201
    And response headers contains keys
      | Location |

  Scenario: Obtaining a coordination by ID
    Given the following payload
      | {"business_key": "myBK"} |
    When I POST to the coordination endpoint
    Then the response status code is 201
    When I GET to the location of the previous POST
    Then the response status code is 200
    And the coordination info is present in the response
    And the coordination state is "RUNNING"
    And response headers contains keys
      | Content-Type |

  Scenario Outline: Obtaining a coordination by business key
    Given the a payload with business key "<bk>" is POST to coordination endpoint
    When I GET a coordination by "<bk>"
    Then the coordination info is present in the response
    And the coordination has "<bk>" as business key
    Examples:
      | bk     |
      | myBk   |
      | yourBK |

  Scenario Outline: Joining executed participants
    Given a started coordination
    When I "join" the participant "<pname>" with "<state_sent>" state
    Then the response status code is 201
    And response headers contains keys
      | Location |
    When I GET to the location of the previous POST
    Then the response status code is 200
     And the participant has the "<state_received>" state
    Examples:
      | action | pname       | state_sent | state_received |
      | join   | CardService |            | JOINED         |
      | join   | CardService | EXECUTED   | EXECUTED       |

  Scenario: Confirming participants of a coordination
    Given a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
     And I "join" the participant "CarService" with "EXECUTED" state
     And I "join" the participant "DinnerService" with "EXECUTED" state
    Then the coordination has 3 participants in "EXECUTED" state
    When I "update" the participant "HotelService" with "CONFIRMED" state
     And I "update" the participant "CarService" with "CONFIRMED" state
     And I "update" the participant "DinnerService" with "CONFIRMED" state
    Then the coordination has 3 participants in "CONFIRMED" state
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "ENDED"

  Scenario: Confirming participants of a coordination
    Given a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
    And I "join" the participant "CarService" with "EXECUTED" state
    And I "join" the participant "DinnerService" with "EXECUTED" state
    Then the coordination has 3 participants in "EXECUTED" state
    When I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    Then the coordination has 2 participants in "CONFIRMED" state
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "INCONSISTENT"