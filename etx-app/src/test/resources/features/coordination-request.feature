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
    Then the coordination has 3 participants with the following states
      | EXECUTED |
    When I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    And I "update" the participant "DinnerService" with "CONFIRMED" state
    Then the coordination has 3 participants with the following states
      | CONFIRMED |
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "ENDED"

  Scenario: Inconsistent coordination
    Given a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
    And I "join" the participant "CarService" with "EXECUTED" state
    And I "join" the participant "DinnerService" with "EXECUTED" state
    Then the coordination has 3 participants with the following states
      | EXECUTED  |
    When I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    Then the coordination has 3 participants with the following states
      | CONFIRMED |
      | EXECUTED  |
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "INCONSISTENT"

  Scenario: Coordination timed out by inconsistency
    Given a configuration with 2 seconds timeout
    And a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
    And I "join" the participant "CarService" with "EXECUTED" state
    And I "join" the participant "DinnerService" with "EXECUTED" state
    And I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    When I wait for 2 seconds
    And I end the coordination
    Then the response status code is 200
    And the coordination state is "ENDED_TIMEOUT"

  Scenario: Coordination rolled back by cancellation of one participant
    Given a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
    And I "join" the participant "CarService" with "EXECUTED" state
    And I "join" the participant "DinnerService" with "EXECUTED" state
    And I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    And I "update" the participant "DinnerService" with "CANCELLED" state
    Then the coordination has 3 participants with the following states
      | CONFIRMED |
      | CANCELLED  |
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "ROLLEDBACK"


  Scenario: Coordination cancelled back by cancellation of all participants
    Given a started coordination
    When I "join" the participant "HotelService" with "EXECUTED" state
    And I "join" the participant "CarService" with "EXECUTED" state
    And I "join" the participant "DinnerService" with "EXECUTED" state
    And I "update" the participant "HotelService" with "CONFIRMED" state
    And I "update" the participant "CarService" with "CONFIRMED" state
    And I "update" the participant "DinnerService" with "CANCELLED" state
    And I "update" the participant "CarService" with "CANCELLED" state
    And I "update" the participant "HotelService" with "CANCELLED" state
    Then the coordination has 3 participants with the following states
      | CANCELLED  |
    When I end the coordination
    Then the response status code is 200
    And the coordination state is "ENDED_CANCELLED"
