function fn() {
  var root = karate.properties['baseUrl'] || 'https://parabank.parasoft.com/parabank';
  return {
    baseUrl: root + '/services/bank',
    username: 'john',
    password: 'demo'
  };
}
