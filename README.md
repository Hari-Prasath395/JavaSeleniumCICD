SampleAutomation - Selenium Java POM Framework

Structure:
- src/main/java/pages
- src/main/java/base
- src/main/java/utils
- src/test/java/tests
- src/test/resources
- reports
- screenshots

How to run:
1. Ensure JDK 21 and Maven are installed.
2. From project root run:

```cmd
mvn -Dtest=tests.LoginTest test
```

Headless (CI) behavior:
- When running in CI (Jenkins) the framework automatically enables headless Chrome by detecting common CI env vars (e.g. JENKINS_HOME, BUILD_ID, CI).
- You can force headless locally by setting the environment variable HEADLESS=true before running tests.
  Example (Windows cmd.exe):

```cmd
set HEADLESS=true
mvn -Dtest=tests.LoginTest test
```

Detailed steps and troubleshooting:
- Ensure `JAVA_HOME` points to a JDK (java -version should report a JDK 21+).
- Ensure `mvn` is on your PATH (run `mvn -version`). If it's not, install Maven: https://maven.apache.org/install.html
- Update Maven dependencies in Eclipse: Right-click project -> Maven -> Update Project... -> Force Update.
- Run from Eclipse: Right-click `LoginTest.java` -> Run As -> TestNG Test (install TestNG for Eclipse if necessary).

Notes:
- Test configuration is in `src/test/resources/config.properties` (url, username, password).
- Reports will be generated in `reports/` and screenshots in `screenshots/` (configured via ExtentManager/Utilities).

Batch helper
- A helper batch script `run-tests.bat` is available to check for Maven and run the test suite.