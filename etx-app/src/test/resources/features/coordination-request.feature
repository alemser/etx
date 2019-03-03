Feature: Handle coordination requests

  Scenario: Create new coordination
    Given the following payload
      | {"business_key": "myBK"} |
    When I POST to the coordination endpoint
    Then the response status code is 201
    And the coordination info is present in the response
    And the coordination state is "RUNNING"
    And response headers contains keys
      | Location |
      | ETag     |

  Scenario: Obtaining a coordination by ID
    Given the following payload
      | {"business_key": "myBK"} |
    When I POST to the coordination endpoint
    Then the response status code is 201
    When I GET to the location of the previous POST
    Then the coordination info is present in the response
    And the coordination state is "RUNNING"
    And response headers contains keys
      | Content-Type |
      | ETag         |

  Scenario Outline: Obtaining a coordination by business key
    Given the a payload with business key "<bk>" is POST to coordination endpoint
    When I GET a coordination by "<bk>"
    Then the coordination info is present in the response
    And the coordination has "<bk>" as business key
    Examples:
      | bk     |
      | myBk   |
      | yourBK |