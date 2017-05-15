#!/usr/bin/env bash

echo "TEST TAGS: --> " ${TEST_TAGS}

if [ "${TEST_TAGS}" == "" ]
then
     echo "empty test tags. nothing to run"
elif [ "${TEST_TAGS}" == "install-dependencies" ]
then
     echo "installing mvn dependencies"
     mvn clean install -DskipTests=true
else
     echo "running '${TEST_TAGS}' tag"
     sh -c  "mvn test -Dcucumber.options='${TEST_TAGS}'"
fi