#!/bin/bash
# Script to rerun only failed test cases locally using testng-failed.xml

FAILED_XML="target/surefire-reports/testng-failed.xml"

if [ -f "$FAILED_XML" ]; then
  echo "Rerunning only failed test cases from $FAILED_XML..."
  mvn test -Dsurefire.suiteXmlFiles=$FAILED_XML
else
  echo "No failed test suite found at $FAILED_XML. Run the full suite first."
fi 