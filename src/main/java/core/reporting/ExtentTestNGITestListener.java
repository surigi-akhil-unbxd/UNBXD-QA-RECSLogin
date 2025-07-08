package core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import lib.Helper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class ExtentTestNGITestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.createInstance("test-output/SparkReport.html");
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public synchronized void onTestStart(ITestResult iTestResult) {
        ExtentTest extentTest = extent.createTest(
            iTestResult.getMethod().getMethodName(),
            iTestResult.getMethod().getDescription()
        );
        test.set(extentTest);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult iTestResult) {
        try {
            appendTestInfoInReport(Status.PASS, iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult iTestResult) {
        try {
            String screenshotPath = Helper.getScreenShot(iTestResult.getMethod().getMethodName());
            if (screenshotPath != null) {
                test.get().fail("Screenshot on Failure",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            }
            appendTestInfoInReport(Status.FAIL, iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult iTestResult) {
        try {
            appendTestInfoInReport(Status.SKIP, iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // Not implemented
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        // No need to set parentTest here
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extent.flush();
    }

    private synchronized void appendTestInfoInReport(Status testStatus, ITestResult iTestResult) throws IOException {
        if (testStatus.equals(Status.FAIL))
            test.get().log(testStatus, "Failure Reason : " + iTestResult.getThrowable().getMessage());
        if (testStatus.equals(Status.SKIP))
            test.get().log(testStatus, "Skipped Reason: " + iTestResult.getThrowable().getMessage());
    }
}
