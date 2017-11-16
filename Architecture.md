## Test Framework Architecture

### Overview

The Framework is designed for Regression Testing that is to execute pre-defined checks (scenarios) expressed in human readable (gherkin) language - BDD style.

Each scenario consists of multiple steps mapped to Java code (step definitions) by Cucumber-JVM. The step definitions can:
1. Interact with Web browser (Chrome) via Selenium library
2. Communicate with API (REST over HTTP) endpoints by sending/receiving JSON or XML using Unirest library.
3. Compare the outcomes of (1 and 2) with expectations defined in scenario steps by utilizing AssertJ library.

Maven build tool wires all the libraries and test code together and runs the test scenarios. They are executed as JUnit tests internally and connected to Cucumber-JVM library in RunCukesTest.class. Cucumber reports all the results in various formats (console output, xml, json, html) which can be consumed by a human or Continuous Integration server (Jenkins).


### Tests Independence

The tests are designed to be independent of each other for more consistent results, easier troubleshooting and ability to run in  parallel. Each test case (scenario) sets its own test environment and data from scratch.  For example, almost every test first creates a new customer before logging-in and placing bets.

The Framework ensures (in Hooks.class) that each test:
1. clears the Storage (with intermediate test data) to start in clean state
2. closes the browser when finished to allow the next test to start in clean state (UI only)
3. takes a screenshot in case of failure (UI only)


### Structure

The Framework architecture decisions were driven by maintainability and simplicity as top priorities.

UI tests constructed with Page Object design pattern. Page Objects are represented as files in /src/test/java/com/tabcorp/qa/wagerplayer/pages/ and correspond to visual WagerPlayer web application pages.  They keep all (CSS) locators to the page elements in one place.  They also contain methods which drive/read the page with Selenium API commands. No other Java classes should know how to drive/read this page.

Similarly to Page Objects, the Framework has files/classes in /src/test/java/com/tabcorp/qa/wagerplayer/api/. Each of them encapsulates all the logic and request/response particulars for corresponding WagerPlayer API (e.g. MOBI_V2). No other Java classes should know how to construct a, say, MOBI_V2 request or where to find error code value in response JSON.

Step definitions are methods grouped in files in /src/test/java/com/tabcorp/qa/wagerplayer/steps/. The grouping is arbitrary by business functions they perform. When possible we place all the related steps definitions in the same class so that they can easily communicate between each other by instance-level variables. If step definitions end up in different groups (classes) but need to communicate then we use Storage.class to keep the state between steps.
A step definition job is to
1. take/parse the input parameters from the Cucumber scenario step
2. call public "api" methods of Page Objects to set the test state or read actual results from UI or API
3. verify the results are as expected

These design principles ensure that when the Framework is maintained the required changes are localized within related class/context rather than spread around the code base.


### Code Reuse

Often times we find that different step definitions need to apply the same or very similar logic. Copy-paste is almost never a good idea (violates DRY principle), so the following techniques are adopted.
- If the logic is only re-used within the same step definitions class - a local helper method is preferred (e.g. CreateEventSteps.prepareAndCreateEvent()).
- But when the logic is so generic that different parts of Framework can re-use it then we keep it as static method in Helpers class. (e.g. Helpers.extractCSV());


### Self Unit Tests
The Framework was designed to strike a pragmatic balance between internal quality and ease/speed of adding tests. It covers only common functions in Helpers class with unit tests for 2 main reasons:
1. The logic is reused across the code base and bugs in it will affect multiple tests
2. It is easy to unit test stateless functions - no mocking is required

Methods in Helpers class are created stateless - they get all the input from parameters and do not modify any state (variables) outside of the its body. Corresponding JUnit tests reside in HelpersTest class
