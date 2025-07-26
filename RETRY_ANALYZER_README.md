# Retry Analyzer Implementation

## Overview
This project includes a comprehensive retry analyzer implementation that automatically retries failed tests to improve test reliability and reduce false failures.

## Components

### 1. RetryAnalyser.java
**Location**: `src/main/java/core/RetryAnalyser.java`

**Features**:
- Configurable retry count (default: 2 retries = 3 total attempts)
- Detailed logging of retry attempts and failure reasons
- Automatic delay between retries (default: 1 second)
- Proper exception handling and thread interruption management

### 2. DataProviderRetryAnalyzer.java
**Location**: `src/main/java/core/DataProviderRetryAnalyzer.java`

**Features**:
- **Specialized for data provider tests** (like ExpTest.java)
- **Retries only failed data sets**, not entire test methods
- Tracks retry attempts per individual data set
- Detailed logging with data set parameters
- Automatic detection of data provider tests

### 3. AnnotationTransformer.java
**Location**: `src/main/java/core/AnnotationTransformer.java`

**Features**:
- **Automatically detects** data provider tests vs regular tests
- **Applies appropriate retry analyzer** based on test type
- No need to add `@RetryAnalyzer` annotation to individual tests
- Error handling and logging for configuration issues

### 4. DataProviderRetryListener.java
**Location**: `src/main/java/core/DataProviderRetryListener.java`

**Features**:
- **Detailed reporting** for data provider retries
- **Retry statistics** at test suite completion
- **Individual data set tracking** and logging
- **Success/failure tracking** with retry counts

### 5. RetryConfig.java
**Location**: `src/main/java/core/RetryConfig.java`

**Features**:
- Centralized configuration management
- System property support for runtime configuration
- Easy configuration modification without code changes

## Configuration

### Default Settings
- **Max Retry Count**: 2 (total 3 attempts per test)
- **Retry Delay**: 1000ms (1 second between retries)

### Runtime Configuration via System Properties

You can override default settings using system properties:

```bash
# Set max retry count to 3 (total 4 attempts)
mvn test -Dtest.retry.max.count=3

# Set retry delay to 2 seconds
mvn test -Dtest.retry.delay.ms=2000

# Combine both settings
mvn test -Dtest.retry.max.count=3 -Dtest.retry.delay.ms=2000
```

### Programmatic Configuration

You can also configure retry settings in your test code:

```java
@Test
public void myTest() {
    // Set custom retry settings for this test
    RetryConfig.setMaxRetryCount(1);
    RetryConfig.setRetryDelayMs(500);
    
    // Your test code here
}
```

## Usage

### Automatic Usage
The retry analyzer is automatically applied to all test methods. No additional configuration is needed.

### Data Provider Tests (Special Feature)
For tests like `ExpTest.java` that use data providers:

**Example Scenario**: You have 5 data sets in `ExperienceTestData.json`:
- Data Set 1: "Home Page" + "WIDGET 1" ✅ (passes)
- Data Set 2: "Product Page" + "WIDGET 3" ❌ (fails)
- Data Set 3: "Category Page" + "WIDGET 2" ✅ (passes)
- Data Set 4: "Brand Page" + "WIDGET 1" ❌ (fails)
- Data Set 5: "Cart Page" + "WIDGET 3" ✅ (passes)

**What Happens**:
- ✅ **Data Set 1, 3, 5**: Pass on first attempt (no retry needed)
- 🔄 **Data Set 2**: Fails → Retries up to 2 times → If still fails, marks as failed
- 🔄 **Data Set 4**: Fails → Retries up to 2 times → If still fails, marks as failed

**Result**: Only the failed data sets are retried, not the entire test method!

### Manual Usage (Optional)
If you want to apply retry analyzer to specific tests only:

```java
@Test(retryAnalyzer = RetryAnalyser.class)
public void specificTest() {
    // This test will use retry analyzer
}

@Test(retryAnalyzer = DataProviderRetryAnalyzer.class)
public void specificDataProviderTest(JsonObject dataMap) {
    // This data provider test will use specialized retry analyzer
}
```

## Logging

The retry analyzer provides detailed logging:

### Info Level
- Retry attempt notifications
- Test method and class information
- Success notifications after retries

### Warning Level
- Failure reasons and exceptions
- Interrupted retry delays

### Severe Level
- Final failure after all retries exhausted

### Example Log Output

#### Regular Tests
```
INFO: Test failed, retrying... Attempt 1 of 2
INFO: Test: testCreateExperience | Class: BoutiqueExperienceTest
WARNING: Failure reason: NoSuchElementException: Widget 'WIDGET 2' not found
INFO: Test passed on retry attempt 1
```

#### Data Provider Tests
```
INFO: Starting data provider test: createExperienceTest | Data Index: 12345
INFO: Data set failed, retrying... Attempt 1 of 2
INFO: Test: createExperienceTest | Class: ExpTest | Data Index: 12345
FINE: Data set parameters: param[0]=JsonObject{"pageName":"Product Page","widgetName":"WIDGET 3"}
WARNING: Failure reason: NoSuchElementException: Widget 'WIDGET 3' not found
INFO: ✅ Data set PASSED after 1 retry attempts: createExperienceTest | Data Index: 12345

📊 Data Provider Retry Statistics:
INFO: Total data sets with retries: 2
INFO: Total retry attempts: 3
INFO:   - ExpTest.createExperienceTest_12345: 1 retries
INFO:   - ExpTest.createExperienceTest_67890: 2 retries
```

## TestNG Configuration

The retry analyzer is configured in `src/test/resources/testng.xml`:

```xml
<suite name="UI Regression Suite">
    <listeners>
        <listener class-name="core.AnnotationTransformer"/>
    </listeners>
    <!-- Test classes here -->
</suite>
```

## Best Practices

1. **Use Appropriate Retry Counts**: 
   - 1-2 retries for flaky UI tests
   - 0 retries for critical business logic tests

2. **Set Reasonable Delays**:
   - 1-2 seconds for UI element loading
   - Longer delays for network-dependent tests

3. **Monitor Logs**:
   - Check retry logs to identify consistently failing tests
   - Use failure reasons to improve test stability

4. **Avoid Over-Retrying**:
   - Don't set retry count too high (max 3-4)
   - Focus on fixing root causes of failures

## Troubleshooting

### Common Issues

1. **Tests Still Failing After Retries**
   - Check if the issue is consistent or intermittent
   - Review failure logs for patterns
   - Consider increasing retry delay

2. **Performance Impact**
   - Reduce retry count for faster feedback
   - Use shorter delays for quick tests

3. **Configuration Not Applied**
   - Verify `AnnotationTransformer` is in testng.xml
   - Check system property syntax
   - Ensure proper package imports

### Debug Mode
Enable debug logging to see detailed retry information:

```bash
mvn test -Djava.util.logging.config.file=logging.properties
```

## Migration from Old Implementation

The new implementation is backward compatible. Existing tests will automatically benefit from:
- Better logging
- Configurable retry settings
- Improved error handling
- Delay between retries

No code changes are required for existing tests. 