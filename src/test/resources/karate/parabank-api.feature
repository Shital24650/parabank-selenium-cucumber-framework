Feature: ParaBank REST API (Karate)

  Background:
    * url baseUrl
    * header Accept = 'application/json'

  Scenario: Login returns the customer
    Given path 'login', username, password
    When method get
    Then status 200
    And match response.id == '#number'
    And match response.firstName == '#string'

  Scenario: Customer has accounts that belong to them
    Given path 'login', username, password
    When method get
    Then status 200
    * def customerId = response.id
    Given path 'customers', customerId, 'accounts'
    When method get
    Then status 200
    And match response == '#[_ > 0]'
    And match each response contains { id: '#number', customerId: '#(customerId)' }
