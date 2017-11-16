## Env vars required

### for Redbook
- WAGERPLAYER_APP_NAME=redbook
- WAGERPLAYER_BASE_URL
- WAGERPLAYER_CASINO_ENV_URL (for casino tests)
- WAGERPLAYER_USERNAME
- WAGERPLAYER_PASSWORD
- WAGERPLAYER_MOBIV2_URL
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME
- WAGERPLAYER_WAPI_PASSWORD
- WAGERPLAYER_CUSTOMER_USERNAME (for tests with pre-existing customers)
- WAGERPLAYER_CUSTOMER_PASSWORD (for tests with pre-existing customers)
- WAGERPLAYER_CLIENT_IP (depends on env)
- WAGERPLAYER_FEEDMQ_PA_HOST
- WAGERPLAYER_FEEDMQ_PA_PORT
- WAGERPLAYER_FEEDMQ_PA_USERNAME
- WAGERPLAYER_FEEDMQ_PA_PASSWORD
- WAGERPLAYER_FEEDMQ_WIFT_HOST
- WAGERPLAYER_FEEDMQ_WIFT_PORT
- WAGERPLAYER_FEEDMQ_WIFT_USERNAME
- WAGERPLAYER_FEEDMQ_WIFT_PASSWORD
- WAGERPLAYER_FEEDMQ_GEARMAN_HOST
- WAGERPLAYER_FEEDMQ_GEARMAN_PORT
- WAGERPLAYER_JWT_CREDIT_COLOSSUS (for external bets)
- WAGERPLAYER_JWT_DEBIT_COLOSSUS (for external bets)
- WAGERPLAYER_JWT_NON_COLOSSUS (for external bets)
- WAGERPLAYER_RUN_MODE=[ DOCKER | DOCKER_LOCAL | <anything-else> ]

### for Luxbet
- WAGERPLAYER_APP_NAME=luxbet
- same as for Redbook (with corresponding values)

### for LuxbetMobile
- WAGERPLAYER_APP_NAME=luxbet
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME=puntclub
- WAGERPLAYER_LUXMOBILE_URL


## Run Tests

### Locally in Terminal/Chrome browser

#### API login
mvn clean test -Dcucumber.options="-t @login -t ~@ui"

#### Sunbets smoke tests
mvn test -Dcucumber.options="-t ~@luxbet -t ~@luxbet-mobile -t ~@casino -t @smoke"

### Locally in SeleniumGRID docker container
- Ensure Docker is installed on your machine. Check by "docker -v". Else download from docker.com
- Start SeleniumGRID docker container: docker run -d -p 4444:4444 -p 5900:5900 selenium/standalone-chrome-debug:3.4.0
- VNC into it: via Safari or a vnc viewer  : vnc://0.0.0.0:5900
- When prompted for password use "secret"
- Change the WAGERPLAYER_RUN_MODE env variable to "DOCKER_LOCAL" (case-sensitive)
- Run tests same way as described in Locally in Terminal/Chrome browser section (above)

### in Potato Cannon docker containers
- set the WAGERPLAYER_RUN_MODE env variable to 'DOCKER' (case-sensitive)
- docker-compose -f "path to the docker-compose.yml"


## Misc

#### Control Log data
Edit src/resources/simplelogger.properties.
For example, to log more details:
- org.slf4j.simpleLogger.defaultLogLevel=debug
#### Test Event names
The base name for test events is set in
src/test/java/com/tabcorp/qa/wagerplayer/Config.java
but can be overwritten in Cucumber steps
#### UI notes
The "Enabled" setting for "Betting" is enabled by default. Hence not part of the feature file.

