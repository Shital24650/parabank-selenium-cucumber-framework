Feature: ParaBank login and accounts

  @smoke @ui
  Scenario: Valid user lands on the accounts overview
    Given I open the ParaBank home page
    When I log in with the default test user
    Then I should see the accounts overview

  @ui
  Scenario: Invalid credentials are rejected
    Given I open the ParaBank home page
    When I log in with username "wrong" and password "wrong"
    Then I should see a login error
    And I should not see the accounts overview
  @api
  Scenario: API login returns the customer
    When I log in through the API with the default test user
    Then the API response status should be 200
    And the API should return a customer id

  @e2e
  Scenario: Accounts in the UI match the accounts from the API
    Given I log in through the API with the default test user
    And I fetch the accounts through the API
    And I open the ParaBank home page
    When I log in with the default test user
    And I read the account ids shown in the UI
    Then the UI account ids should match the API account ids
