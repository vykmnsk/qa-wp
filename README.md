#Run Tests
mvn test -Dcucumber.options="--name Login"

#Debug in ItelliJ
mvn test -DforkMode=never

#Env vars required
- WAGERPLAYER_APP_NAME=[luxbet|redbook]
- WAGERPLAYER_BASE_URL
- WAGERPLAYER_USERNAME
- WAGERPLAYER_PASSWORD
- WAGERPLAYER_WAPI_URL
- WAGERPLAYER_WAPI_USERNAME
- WAGERPLAYER_WAPI_PASSWORD
- WAGERPLAYER_CUSTOMER_USERNAME
- WAGERPLAYER_CUSTOMER_PASSWORD
- RUN_MODE=[ DOCKER | anything-else ]

#Docker Run

- docker-compose -f "path to the docker-compose.yml"   &
- set the RUN_MODE env variable to 'DOCKER' to run in conatiners. anything else or not setting it, will make the test run use Chrome Driver.
- vnc into localhost:5900 to see the test execution and the passsword is 'secret' . Use safari if there , becasuse it has inbuilt vnc support. vnc://localhost:5900
- run the tests
- and then one should be able to see the test runs in the conatiner.
