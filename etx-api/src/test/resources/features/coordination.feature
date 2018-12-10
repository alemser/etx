Feature: Handle coordination

  Scenario: Coordination start
    When a coordination is started
    Then an id is created
    And the coordination state is "RUNNING"
    And there are 0 participants

  Scenario: Joining participants
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    Then participants are in the "JOINED" state
      |CardService|
      |FraudService|
      |HotelService|

  Scenario: Executing participants
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    Then participants are in the "EXECUTED" state
      |CardService|
      |FraudService|
      |HotelService|

  Scenario: Confirming participants
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |FraudService|
      |HotelService|
    Then participants are in the "CONFIRMED" state
      |CardService|
      |FraudService|
      |HotelService|

  Scenario: Cancelling participants
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "cancel" the participants
      |CardService|
      |FraudService|
      |HotelService|
    Then participants are in the "CANCELLED" state
      |CardService|
      |FraudService|
      |HotelService|

  Scenario: Ending a successful coordination
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I end the coordination in 1 "MILLISECONDS" from now
    Then the coordination state is "ENDED"

  Scenario: Ending a coordination with no participants
    When a coordination is started
    And I end the coordination in 1 "MILLISECONDS" from now
    Then the coordination state is "ENDED"

  @handleExceptions
  Scenario: Adding participants to an ended coordination
    Given a coordination is started
    And I end the coordination in 1 "MILLISECONDS" from now
    When I "join" the participants
      |CardService|
    Then an exception with message "Cannot join to not running coordination" is throw

  Scenario: Ending inconsistent coordination
    When a coordination is started
    And I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |HotelService|
    And I end the coordination in 1 "MILLISECONDS" from now
    Then the coordination state is "INCONSISTENT"

  Scenario: Ending inconsistent coordination by timeout
    When a coordination is started
    And I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |HotelService|
    And I end the coordination in 5 "SECONDS" from now
    Then the coordination state is "ENDED_TIMEOUT"

  Scenario: Cancellation of a participant
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |FraudService|
    And I "cancel" the participants
      |HotelService|
    Then participants are in the "CANCELLED" state
      |HotelService|
    And I end the coordination in 1 "MILLISECONDS" from now
    And the coordination state is "ROLLEDBACK"

  Scenario: Ending an unsuccessful coordination
    Given a coordination is started
    When I "join" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "execute" the participants
      |CardService|
      |FraudService|
      |HotelService|
    And I "confirm" the participants
      |CardService|
      |FraudService|
    And I "cancel" the participants
      |HotelService|
    Then participants are in the "CANCELLED" state
      |HotelService|
    And I end the coordination in 1 "MILLISECONDS" from now
    And the coordination state is "ROLLEDBACK"
    And I "cancel" the participants
      |CardService|
      |FraudService|
    And I end the coordination in 1 "MILLISECONDS" from now
    And the coordination state is "ENDED_CANCELLED"

  Scenario: Scheduled ended inconsistent coordinations
    When a coordination is started
    And I "join" the participants
      |CardService|
      |FraudService|
    And I "execute" the participants
      |CardService|
      |FraudService|
    And I "confirm" the participants
      |CardService|
    And I end the coordination in 1 "MILLISECONDS" from now
    Then the coordination state is "INCONSISTENT"
    When I end the coordination in 4 "SECONDS" from now
    Then the coordination state is "ENDED_TIMEOUT"