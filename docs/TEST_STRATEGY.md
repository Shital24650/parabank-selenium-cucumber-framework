# Test strategy: ParaBank

## Scope
Login and accounts overview, through the UI and the REST API, plus a UI-vs-API consistency check.
Out of scope for now: transfers, bill pay, performance, accessibility, security testing.

## Test levels
| Level | What | Tooling | Network |
|---|---|---|---|
| Unit | Response parsing with mocked `Response` objects | TestNG, Mockito | No |
| Component | `ApiClient` against a stubbed ParaBank, incl. 400 and 500 responses | WireMock | No (local stub) |
| API | REST endpoints on the real system | RestAssured, Karate | Yes |
| UI | Page flows | Selenium, Cucumber (BDD) | Yes |
| E2E | UI and API agree on the same data | Selenium + RestAssured | Yes |

Unit and component tests run first and fast. Only if they pass do the slower functional tests run.

## Approach
- Risk first: login and account data are the highest-impact flows, so they are covered first.
- Scenarios are written in Gherkin so they double as acceptance criteria and can be reviewed without reading Java.
- API checks avoid the browser; the browser starts only for scenarios that need it.
- Negative cases are covered in the stubbed tests, where error responses can be produced on demand.

## Entry and exit criteria
- Entry: environment reachable (health check on `index.htm`), test data initialised.
- Exit: all non-known-issue scenarios pass; every failure is either fixed or logged as a defect with evidence.

## Defect handling
Report with: scenario name, steps, expected vs actual, environment, response body or screenshot, and the CI run link.
Known product defects are tagged `@known-issue` and excluded from the default run so that CI stays trustworthy. Each excluded scenario is listed in the README.

## Estimation (rough, per scenario)
| Activity | Typical effort |
|---|---|
| Understand requirement, write the scenario | 0.5 h |
| Implement steps and page object changes | 1-2 h |
| Stabilise (waits, data) and review | 0.5-1 h |
A new scenario is usually 2-3.5 h. Add time when a new page object or new test data setup is needed.

## Metrics
`scripts/metrics.py` reads `target/cucumber.json` and reports scenarios run, pass/fail/skipped, pass rate, execution time and results per tag. CI shows it on the run summary; Jenkins archives it.

## Risks
| Risk | Mitigation |
|---|---|
| Shared public site changes or is down | Local ParaBank via `docker-compose.yml`; re-run before debugging 5xx |
| Shared `john` account state | Roadmap: register a fresh customer per run |
| Flaky UI timing | Explicit waits only, no `Thread.sleep` |
