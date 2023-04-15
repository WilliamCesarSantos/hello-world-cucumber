Feature: Movie crud

  Scenario: The terminator was not found
    Given The movie "The terminator" was not registered
    When Create "The terminator","Ação",10 movie
    Then Response has status code 201

  Scenario: Try register movie with rating equals 15 - not success
    Given The movie "O auto da compadecida" was not registered
    When Create "O auto da compadecida","Comédia",15 movie
    Then Response has status code 400

  Scenario: Try register movie without genre - not success
    Given The movie "O auto da compadecida" was not registered
    When Create "O auto da compadecida","null",15 movie
    Then Response has status code 400