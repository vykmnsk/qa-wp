#Env vars required
### for Redbook
- WAGERPLAYER_APP_NAME=redbook
- WAGERPLAYER_BASE_URL
- WAGERPLAYER_USERNAME
- WAGERPLAYER_PASSWORD
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME
- WAGERPLAYER_WAPI_PASSWORD
- WAGERPLAYER_CUSTOMER_USERNAME
- WAGERPLAYER_CUSTOMER_PASSWORD
- WAGERPLAYER_MOBIV2_URL
- WAGERPLAYER_RUN_MODE=[ DOCKER | anything-else ]

### for Luxbet
- WAGERPLAYER_APP_NAME=luxbet
- WAGERPLAYER_BASE_URL
- WAGERPLAYER_USERNAME
- WAGERPLAYER_PASSWORD
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME
- WAGERPLAYER_WAPI_PASSWORD
- WAGERPLAYER_CUSTOMER_USERNAME
- WAGERPLAYER_CUSTOMER_PASSWORD
- WAGERPLAYER_CLIENT_IP
- WAGERPLAYER_RUN_MODE=[ DOCKER | anything-else ]

### for LuxbetMobile
- WAGERPLAYER_APP_NAME=luxbet
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME=puntclub
- WAGERPLAYER_LUXMOBILE_URL
 

#Run Tests
- mvn test -Dcucumber.options="--name Login"
- mvn test -Dcucumber.options="-t @create-customer"

#Debug in ItelliJ
mvn test -DforkMode=never

# Info

- The base name for races is :  TEST RACE
- The "Enabled" setting for "Betting" is enabled by 
  default. Hence not part of the feature file.

# Running Tests on your machine using Docker

- Ensure Docker is installed on your machine. Check by "docker -v". Else download from docker.com
- Change the RUN_MODE env variable to "DOCKER" (Case Sensitive)
- Change the remote driver to point to "http://localhost:4444/wd/hub" because it points to localhost
- Setup the SeleniumGRID using the cmd : docker run -d -p 4444:4444 -p 5900:5900 selenium/standalone-chrome-debug:3.4.0
- vnc : into it --> Safari or Use a vnc viewer  : vnc://0.0.0.0:5900 
- When prompted for password use "secret"
- Run test cases and see it running.

# Docker Run

- docker-compose -f "path to the docker-compose.yml"   &
- set the RUN_MODE env variable to 'DOCKER' to run in conatiners. anything else or not setting it, will make the test run use Chrome Driver.
- vnc into localhost:5900 to see the test execution and the passsword is 'secret' . Use safari if there , becasuse it has inbuilt vnc support. vnc://localhost:5900
- run the tests
- and then one should be able to see the test runs in the conatiner.

# Confluence Link
   [Click-this](http://wiki.corpad.net.local:8090/display/WTG/qa-wagerplayer+code+setup)
