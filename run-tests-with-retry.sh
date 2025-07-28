#!/bin/bash

# Test Execution Script with Two-Phase Approach
# Phase 1: Run full test suite (no retries)
# Phase 2: Run only failed tests (with retry analyzer)

set -e  # Exit on any error

echo "=========================================="
echo "Starting Two-Phase Test Execution"
echo "=========================================="

# Configuration
TESTNG_XML="src/test/resources/testng.xml"
RETRY_XML="src/test/resources/testng-retry.xml"
FAILED_XML="test-output/testng-failed.xml"
LOG_DIR="test-output"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Create log directory
mkdir -p $LOG_DIR

echo "Phase 1: Running Full Test Suite (No Retries)"
echo "=============================================="

# Phase 1: Run full test suite with no retry analyzer
echo "Running initial test suite..."
mvn clean test -DsuiteXmlFile=$TESTNG_XML -Dtest.retry.enabled=false > $LOG_DIR/phase1_$TIMESTAMP.log 2>&1

# Capture exit code from Phase 1
PHASE1_EXIT_CODE=$?

echo "Phase 1 completed with exit code: $PHASE1_EXIT_CODE"

# Check if there are any failed tests
if [ -f "$FAILED_XML" ]; then
    echo ""
    echo "Phase 2: Running Failed Tests with Retry Analyzer"
    echo "=================================================="
    
    # Count failed tests
    FAILED_COUNT=$(grep -c "<test-method" "$FAILED_XML" || echo "0")
    echo "Found $FAILED_COUNT failed test(s) to retry"
    
    if [ "$FAILED_COUNT" -gt 0 ]; then
        echo "Running failed tests with retry analyzer..."
        
        # Phase 2: Run failed tests with retry analyzer
        mvn test -DsuiteXmlFile=$RETRY_XML -Dtest.retry.enabled=true > $LOG_DIR/phase2_$TIMESTAMP.log 2>&1
        
        PHASE2_EXIT_CODE=$?
        echo "Phase 2 completed with exit code: $PHASE2_EXIT_CODE"
        
        # Final status
        if [ $PHASE2_EXIT_CODE -eq 0 ]; then
            echo ""
            echo "✅ SUCCESS: All failed tests passed on retry!"
            echo "=========================================="
            exit 0
        else
            echo ""
            echo "❌ FAILURE: Some tests still failed after retry"
            echo "=============================================="
            exit $PHASE2_EXIT_CODE
        fi
    else
        echo "No failed tests found to retry"
        exit $PHASE1_EXIT_CODE
    fi
else
    echo "No failed tests XML found - all tests passed in Phase 1!"
    echo "========================================================"
    exit $PHASE1_EXIT_CODE
fi 