<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns9:loginRequest xmlns:ns14="http://www.playtech.com/services/responsiblegaming"
                  xmlns:ns15="http://www.playtech.com/services/casino-configuration"
                  xmlns:ns9="http://www.playtech.com/services/external/login"
                  xmlns:ns5="http://www.playtech.com/services/login"
                  xmlns:ns12="http://www.playtech.com/services/bonus"
                  xmlns:ns6="http://www.playtech.com/services/player-management"
                  xmlns:ns13="http://www.playtech.com/services/player-payment"
                  xmlns:ns7="http://www.playtech.com/services/external/batch"
                  xmlns:ns10="http://www.playtech.com/services/external/player-management"
                  xmlns:ns8="http://www.playtech.com/services/external/wallet"
                  xmlns:ns11="http://www.playtech.com/services/rule-management"
                  xmlns:ns2="http://www.playtech.com/services/common"
                  xmlns:ns4="http://www.playtech.com/services/wallet"
                  xmlns:ns3="http://www.playtech.com/services/batch">

    <ns9:authenticationByPassword>
        <ns2:password>${password}</ns2:password>
    </ns9:authenticationByPassword>

    <ns9:playerIdentityByCasinoAndUsername>
        <ns2:username>${username}</ns2:username>
    </ns9:playerIdentityByCasinoAndUsername>

</ns9:loginRequest>