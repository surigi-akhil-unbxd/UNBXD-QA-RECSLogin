package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {

    private int count =0;
    private static int maxCount=0;

    @Override
    public boolean retry(ITestResult iTestResult) {

        if(!iTestResult.isSuccess())
        {
            if(count<maxCount) {
                count++;
                iTestResult.setStatus(ITestResult.FAILURE);
                return  true;
            }
            else
                iTestResult.setStatus(iTestResult.FAILURE);
        }
        else
        {
            iTestResult.setStatus(iTestResult.SUCCESS);
        }
        return false;
    }
}
