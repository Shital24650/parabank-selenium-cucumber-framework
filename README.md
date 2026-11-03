# # ParaBank Test Automation Framework

UI + API test automation for the public ParaBank demo bank.

**Stack:** Java 17, Selenium WebDriver, Cucumber (BDD), TestNG, RestAssured, Maven, Page Object Model, GitHub Actions.

## Structure
- `pages/` page objects (locators and actions)
- `steps/` Cucumber step definitions and hooks
- `utils/` driver factory, config reader, REST client
- `runners/` TestNG and Cucumber runner
- `src/test/resources/features/` Gherkin scenarios

## Scenarios
- `@ui` valid and invalid login
- `@api` REST login returns the customer
- `@e2e` accounts shown in the UI match accounts returned by the API

## Run
    mvn test                          # headless
    mvn test -Dheadless=false         # visible browser
    mvn test -Dcucumber.filter.tags="@api"

CI runs on every push. The HTML report is attached to each run as an artifact.

## Notes
Tests run against the shared public ParaBank site (john / demo), which can be slow or briefly unavailable.
