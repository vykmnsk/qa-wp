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
mvn clean test -Dcucumber.options="-t @login"

#Debug in ItelliJ
mvn clean test -DforkMode=never ...

# Info
- The base name for test events is set in Config.java but can be overwritten in Cucumber steps
- The "Enabled" setting for "Betting" is enabled by 
  default. Hence not part of the feature file.

#Docker Run
- docker-compose -f "path to the docker-compose.yml"   &
- set the RUN_MODE env variable to 'DOCKER' to run in conatiners. anything else or not setting it, will make the test run use Chrome Driver.
- vnc into localhost:5900 to see the test execution and the passsword is 'secret' . Use safari if there , becasuse it has inbuilt vnc support. vnc://localhost:5900
- run the tests
- and then one should be able to see the test runs in the conatiner.

#Confluence Link
   [Click-this](http://wiki.corpad.net.local:8090/display/WTG/qa-wagerplayer+code+setup)
