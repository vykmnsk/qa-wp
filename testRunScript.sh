#!/usr/bin/env bash

mvn test -Dcucumber.options="-t @create-customer -t @redbook -t @ui -t ~@credit-card"
mvn test -Dcucumber.options="-t @login"

# Devs working on the fix affected testcases below :

#mvn test -Dcucumber.options="-t @create-customer -t @redbook -t @api"
#mvn test -Dcucumber.options="-t @redbook -t @create-customer -t @credit-card -t @api"

