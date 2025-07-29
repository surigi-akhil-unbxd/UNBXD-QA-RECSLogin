package core;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Configuration class for retry analyzer settings
 * Centralizes retry configuration for easy maintenance
 */
public class RetryConfig {
    
    private static final Logger logger = Logger.getLogger(RetryConfig.class.getName());
    
    // Default retry configuration
    private static final int DEFAULT_MAX_RETRY_COUNT = 2;
    private static final long DEFAULT_RETRY_DELAY_MS = 1000; // 1 second delay between retries
    
    // Configurable retry settings (can be overridden via system properties)
    private static int maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
    private static long retryDelayMs = DEFAULT_RETRY_DELAY_MS;
    
    static {
        // Load configuration from system properties if available
        loadConfiguration();
    }
    
    /**
     * Load retry configuration from system properties
     */
    private static void loadConfiguration() {
        try {
            String maxRetryProp = System.getProperty("test.retry.max.count");
            if (maxRetryProp != null) {
                maxRetryCount = Integer.parseInt(maxRetryProp);
                logger.log(Level.INFO, "Loaded max retry count from system property: " + maxRetryCount);
            }
            
            String retryDelayProp = System.getProperty("test.retry.delay.ms");
            if (retryDelayProp != null) {
                retryDelayMs = Long.parseLong(retryDelayProp);
                logger.log(Level.INFO, "Loaded retry delay from system property: " + retryDelayMs + "ms");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid retry configuration in system properties, using defaults", e);
        }
    }
    
    /**
     * Get the maximum number of retry attempts
     */
    public static int getMaxRetryCount() {
        return maxRetryCount;
    }
    
    /**
     * Get the delay between retry attempts in milliseconds
     */
    public static long getRetryDelayMs() {
        return retryDelayMs;
    }
    
    /**
     * Set the maximum number of retry attempts
     */
    public static void setMaxRetryCount(int count) {
        maxRetryCount = count;
        logger.log(Level.INFO, "Max retry count set to: " + count);
    }
    
    /**
     * Set the delay between retry attempts
     */
    public static void setRetryDelayMs(long delayMs) {
        retryDelayMs = delayMs;
        logger.log(Level.INFO, "Retry delay set to: " + delayMs + "ms");
    }
    
    /**
     * Reset configuration to defaults
     */
    public static void resetToDefaults() {
        maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
        retryDelayMs = DEFAULT_RETRY_DELAY_MS;
        logger.log(Level.INFO, "Retry configuration reset to defaults");
    }
    
    /**
     * Get current configuration as string for logging
     */
    public static String getConfigurationInfo() {
        return String.format("RetryConfig: maxRetryCount=%d, retryDelayMs=%d", maxRetryCount, retryDelayMs);
    }
} 