package core;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AnnotationTransformer implements IAnnotationTransformer {
    
    private static final Logger logger = Logger.getLogger(AnnotationTransformer.class.getName());

    @Override
    public synchronized void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        try {
            // Check if this is a data provider test
            boolean isDataProviderTest = isDataProviderTest(testMethod);
            
            if (isDataProviderTest) {
                // Use specialized retry analyzer for data provider tests
                annotation.setRetryAnalyzer(DataProviderRetryAnalyzer.class);
                logger.log(Level.FINE, "Applied DataProviderRetryAnalyzer to data provider test: " + 
                          (testMethod != null ? testMethod.getName() : "unknown"));
            } else {
                // Use standard retry analyzer for regular tests
                annotation.setRetryAnalyzer(RetryAnalyser.class);
                logger.log(Level.FINE, "Applied standard RetryAnalyser to test: " + 
                          (testMethod != null ? testMethod.getName() : "unknown"));
            }
            
            // Log which test method is being configured
            if (testMethod != null) {
                logger.log(Level.FINE, "Applied retry analyzer to test method: " + testMethod.getName());
            } else if (testClass != null) {
                logger.log(Level.FINE, "Applied retry analyzer to test class: " + testClass.getSimpleName());
            }
            
            // Optional: Set additional test configuration
            // annotation.setInvocationCount(1); // Default invocation count
            // annotation.setTimeOut(30000); // Default timeout in milliseconds
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error applying retry analyzer: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if the test method is a data provider test
     */
    private boolean isDataProviderTest(Method testMethod) {
        if (testMethod == null) {
            return false;
        }
        
        try {
            // Check for @Test annotation with dataProvider attribute
            org.testng.annotations.Test testAnnotation = testMethod.getAnnotation(org.testng.annotations.Test.class);
            if (testAnnotation != null && testAnnotation.dataProvider() != null && !testAnnotation.dataProvider().isEmpty()) {
                return true;
            }
            
            // Check for @DataProvider annotation
            if (testMethod.isAnnotationPresent(DataProvider.class)) {
                return true;
            }
            
            // Check if method has parameters (indicates data provider usage)
            return testMethod.getParameterCount() > 0;
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error checking if test is data provider test", e);
            return false;
        }
    }
}
