#!/bin/bash
# Run the full suite, then automatically rerun failed cases if any

echo "Running full test suite..."
mvn test

FAILED_XML="target/surefire-reports/testng-failed.xml"

if [ -f "$FAILED_XML" ]; then
  echo "Detected failed tests. Rerunning only failed test cases from $FAILED_XML..."
  mvn test -Dsurefire.suiteXmlFiles=$FAILED_XML
else
  echo "No failed test suite found. All tests passed on the first run!"
fi 