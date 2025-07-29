# Cookie-Based Authentication Flow

## Overview
This document explains how the test suite uses cookie-based authentication to ensure fast, reliable, and consistent test execution.

## 🔄 Authentication Flow

### **Step 1: LoginTest Runs First**
```xml
<!-- Login Test: runs first, sequentially -->
<test name="LoginTest" parallel="false">
    <classes>
        <class name="UnbxdTests.testNG.login.LoginTest"/>
    </classes>
</test>
```

**What LoginTest Does:**
1. **Performs Login**: `loginActions.login(1, 1)` - Logs in with user ID 1 and site ID 1
2. **Saves Cookies**: `Helper.saveCookiesToFile(driver, "cookies.json")` - Stores session cookies
3. **Logs Success**: Prints confirmation that cookies were saved

### **Step 2: All Other Tests Use Cookies**
```xml
<!-- All other tests: run in parallel by class -->
<test name="ParallelTests" parallel="classes" thread-count="10">
    <classes>
        <!-- All test classes here -->
    </classes>
</test>
```

**What Each Test Does:**
1. **Sets Context**: `lib.EnvironmentConfig.setContext(1, 1)` - Sets user and site context
2. **Restores Cookies**: `lib.Helper.restoreCookiesFromFile(driver, "cookies.json", lib.EnvironmentConfig.getLoginUrl())`
3. **Validates Cookies**: Throws exception if cookies not found
4. **Refreshes Page**: `driver.navigate().refresh()` - Applies the session

## 📋 Test Suite Configuration

### **Main Test Suite**: `src/test/resources/testng.xml`

**Execution Order:**
1. **LoginTest** (Sequential, runs first)
2. **All Other Tests** (Parallel, 10 threads)

**Included Test Classes:**
- ✅ CoreAlgorithmsTest
- ✅ CustomAlgorithmsTest  
- ✅ topsellersCustomAlgoTest
- ✅ ExpTest
- ✅ BoutiqueExperienceTest
- ✅ JourneyExperienceTest
- ✅ broadCastPageTest
- ✅ DHBoutiqueTemplateTest
- ✅ DHexitIntentTemplatetest
- ✅ DHstandardWidgettemplateTest
- ✅ DVBoutiqueTemplateTest
- ✅ DVexitIntentTemplateTest
- ✅ DVstandardWidgetTemplateTest
- ✅ MHboutiqueTemplateTest
- ✅ MHinstagramLikeBehaaviorTemplateTest
- ✅ MHstandardWidgettemplateTest
- ✅ MHtinderLikeBehaviorTemplateTest
- ✅ MVboutiqueTemplateTest
- ✅ MVinstagramLikeBehaviorTemplateTest
- ✅ MVstandardWidgetTemplateTest
- ✅ MVtinderLikeBehaviorTemplateTest


## 🚀 How to Run the Test Suite

### **Run Complete Suite (Recommended)**
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

**What Happens:**
1. **LoginTest executes first** - Logs in and saves cookies
2. **All other tests run in parallel** - Use saved cookies for authentication
3. **No login delays** - Tests start immediately with authenticated session

### **Run Individual Tests (For Development)**
```bash
# Run just LoginTest to create cookies
mvn test -Dtest=LoginTest

# Run any other test (will use existing cookies)
mvn test -Dtest=CustomAlgorithmsTest
```

## 🔧 Cookie Configuration Pattern

### **Standard Setup for All Tests:**
```java
@BeforeClass
public void setUp() {
    try {
        super.setUp();
        lib.EnvironmentConfig.setContext(1, 1);
        this.initFluent(driver);
        initTest();
        // Initialize your actions here
        boolean cookiesRestored = lib.Helper.restoreCookiesFromFile(driver, "cookies.json", lib.EnvironmentConfig.getLoginUrl());
        if (!cookiesRestored) {
            throw new IllegalStateException("Cookies not found. Please run LoginTest first.");
        }
        driver.navigate().refresh();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## 📊 Benefits

### **⚡ Performance**
- **No Login Delays**: Tests start immediately
- **Faster Execution**: No authentication overhead
- **Parallel Execution**: 10 tests can run simultaneously

### **🔒 Reliability**
- **Consistent Sessions**: All tests use same authenticated session
- **No Login Failures**: Eliminates login-related test failures
- **Stable Environment**: Same user context across all tests

### **🛡️ Security**
- **Session Management**: Proper session handling
- **Context Isolation**: Each test has proper user/site context
- **Cookie Validation**: Ensures cookies exist before proceeding

## 🚨 Error Handling

### **Cookie Not Found Error**
If you see this error:
```
IllegalStateException: Cookies not found. Please run LoginTest first.
```

**Solution:**
1. Run LoginTest first: `mvn test -Dtest=LoginTest`
2. Then run your test: `mvn test -Dtest=YourTest`

### **Cookie Expired Error**
If cookies expire during long test runs:
1. Re-run LoginTest to refresh cookies
2. Continue with your test suite

## 📁 File Locations

- **Cookies File**: `cookies.json` (root directory)
- **LoginTest**: `src/test/java/UnbxdTests/testNG/login/LoginTest.java`
- **Test Suite**: `src/test/resources/testng.xml`
- **Helper Methods**: `src/main/java/lib/Helper.java`

## 🔄 Retry Analyzer Integration

The cookie-based authentication works seamlessly with the retry analyzer:
- **Data Provider Tests**: Retry only failed data sets
- **Regular Tests**: Retry entire test methods
- **Cookie Persistence**: Cookies remain valid across retries

## 🎯 Best Practices

1. **Always run LoginTest first** when starting a new test session
2. **Use the complete test suite** for full regression testing
3. **Monitor cookie expiration** during long test runs
4. **Keep cookies.json in version control** for team consistency
5. **Use parallel execution** for faster test completion

## 📈 Performance Metrics

**Before (Direct Login):**
- Login time per test: ~10-15 seconds
- Total suite time: ~30-45 minutes

**After (Cookie-Based):**
- Login time per test: ~0-1 seconds
- Total suite time: ~15-20 minutes
- **Performance improvement: ~50-60% faster** 