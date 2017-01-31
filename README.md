#Run Application
mvn test -Dcucumber.options="--name Login"

#Debug in ItelliJ
mvn test -DforkMode=never

#Env vars required
- WAGERPLAYER_APP_ID=[luxbet|redbook]
- WAGERPLAYER_BASE_URL
- WAGERPLAYER_USERNAME
- WAGERPLAYER_PASSWORD