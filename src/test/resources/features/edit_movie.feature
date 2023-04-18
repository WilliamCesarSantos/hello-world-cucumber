Feature: Edit movie

  Scenario: I need update title of the movie already registered
    Given I have movie registered
      #data table
    When I apply update on movie
      #data table
    Then found the movie in database
    And the rating equals to
    And The response should have status equals 200