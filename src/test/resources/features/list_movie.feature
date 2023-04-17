Feature: List movies

  Scenario: I have registered movie
    Given I have "The terminator" movie registered
    When I search the movie by title
    Then I should found "The terminator" movie
    And The response should have status equals 200

  Scenario: I don't have movie registered
    Given that I don't have the "Xuxa - Rainha dos baixinhos" movie registered
    When I search the movie by title
    Then I shouldn't found any movie
    And The response should have status equals 200