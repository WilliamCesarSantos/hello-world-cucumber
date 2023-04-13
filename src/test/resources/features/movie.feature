Feature: Movie crud
  Scenario: The terminator was not found
    Given The movie "The terminator" was not registered
    When Create "The terminator","Ação",10 movie
    Then The movie "The terminator" is list