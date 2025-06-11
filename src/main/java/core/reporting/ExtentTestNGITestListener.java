package core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import lib.Helper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

public class ExtentTestNGITestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.createInstance("extent.html");
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();

    ExtentTest parent, subChild;
    ITestContext context;


    @Override
    public synchronized void onTestStart(ITestResult iTestResult) {


    }

    @Override
    public synchronized void onTestSuccess(ITestResult iTestResult) {

        ExtentTest child = parentTest.get().createNode(" Test " + iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription());
        test.set(child);
        try {
            appendTestInfoInReport(Status.PASS, iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void onTestFailure(ITestResult iTestResult) {
        ExtentTest child = parentTest.get().createNode("Test :" + iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription());
        test.set(child);
        try {
            // Take screenshot and attach to report
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

        ExtentTest child = parentTest.get().createNode("Test :" + iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription());
        test.set(child);
        try {
            appendTestInfoInReport(Status.SKIP, iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public   synchronized void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public  synchronized void onStart(ITestContext iTestContext) {
        this.context = iTestContext;
        parent = extent.createTest(context.getName());
        parentTest.set(parent);
    }

    @Override
    public synchronized void onFinish(ITestContext iTestContext) {
        extent.flush();

    }


    private synchronized void appendTestInfoInReport(Status testStatus, ITestResult iTestResult) throws IOException
    {
        //String destinationPath = Helper.getScreenShot(iTestResult.getMethod().getMethodName());

       // if (destinationPath != null) {
        //    test.get().addScreenCaptureFromPath(new File(destinationPath).getPath());
        //}
        if (testStatus.equals(Status.FAIL))
            test.get().log(testStatus, "Failure Reason : " + iTestResult.getThrowable().getMessage());
        if (testStatus.equals(Status.SKIP))
            test.get().log(testStatus, "Skipped Reason: " + iTestResult.getThrowable().getMessage());
        }

}
