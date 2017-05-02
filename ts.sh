#!/usr/bin/env bash

mvn test -Dcucumber.options="-t @create-customer -t @redbook -t @ui"