package core;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;

/**
 * Test listener for data provider retry functionality
 * Provides detailed reporting and management of data provider retries
 */
public class DataProviderRetryListener implements ITestListener {
    
    private static final Logger logger = Logger.getLogger(DataProviderRetryListener.class.getName());
    
    @Override
    public void onTestStart(ITestResult result) {
        // Log when a data provider test starts
        if (isDataProviderTest(result)) {
            logger.log(Level.INFO, "Starting data provider test: " + result.getMethod().getMethodName() + 
                       " | Data Index: " + getDataIndex(result));
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        if (isDataProviderTest(result)) {
            String dataSetKey = getDataSetKey(result);
            int retryCount = DataProviderRetryAnalyzer.getRetryCount(dataSetKey);
            
            if (retryCount > 0) {
                logger.log(Level.INFO, "✅ Data set PASSED after " + retryCount + " retry attempts: " + 
                           result.getMethod().getMethodName() + " | Data Index: " + getDataIndex(result));
            } else {
                logger.log(Level.INFO, "✅ Data set PASSED on first attempt: " + 
                           result.getMethod().getMethodName() + " | Data Index: " + getDataIndex(result));
            }
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        if (isDataProviderTest(result)) {
            String dataSetKey = getDataSetKey(result);
            int retryCount = DataProviderRetryAnalyzer.getRetryCount(dataSetKey);
            
            logger.log(Level.SEVERE, "❌ Data set FAILED after " + retryCount + " retry attempts: " + 
                       result.getMethod().getMethodName() + " | Data Index: " + getDataIndex(result));
            
            if (result.getThrowable() != null) {
                logger.log(Level.SEVERE, "Failure reason: " + result.getThrowable().getMessage());
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        if (isDataProviderTest(result)) {
            logger.log(Level.WARNING, "⚠️ Data set SKIPPED: " + result.getMethod().getMethodName() + 
                       " | Data Index: " + getDataIndex(result));
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        // Print retry statistics at the end of test suite
        Map<String, Integer> retryStats = DataProviderRetryAnalyzer.getRetryStatistics();
        
        if (!retryStats.isEmpty()) {
            logger.log(Level.INFO, "📊 Data Provider Retry Statistics:");
            logger.log(Level.INFO, "Total data sets with retries: " + retryStats.size());
            
            int totalRetries = retryStats.values().stream().mapToInt(Integer::intValue).sum();
            logger.log(Level.INFO, "Total retry attempts: " + totalRetries);
            
            // Log individual retry counts
            retryStats.forEach((dataSetKey, retryCount) -> {
                logger.log(Level.INFO, "  - " + dataSetKey + ": " + retryCount + " retries");
            });
        }
        
        // Clear retry statistics for next test suite
        DataProviderRetryAnalyzer.clearAllRetryCounts();
    }
    
    /**
     * Check if the test result is from a data provider test
     */
    private boolean isDataProviderTest(ITestResult result) {
        try {
            Object[] parameters = result.getParameters();
            return parameters != null && parameters.length > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate a unique key for the data set
     */
    private String getDataSetKey(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        int dataIndex = getDataIndex(result);
        return className + "." + methodName + "_" + dataIndex;
    }
    
    /**
     * Get the data index from the test result
     */
    private int getDataIndex(ITestResult result) {
        try {
            Object[] parameters = result.getParameters();
            if (parameters != null && parameters.length > 0) {
                return result.getInstance().hashCode();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error getting data index", e);
        }
        return 0;
    }
} 