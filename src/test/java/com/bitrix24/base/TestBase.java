package com.bitrix24.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.bitrix24.pages.LoginPage;
import com.bitrix24.pages.Message;
import com.bitrix24.pages.Poll;
import com.bitrix24.pages.Topic;
import com.bitrix24.utilities.BrowserUtilities;
import com.bitrix24.utilities.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.bitrix24.utilities.ConfigurationReader;

import java.io.IOException;


public class TestBase {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ExtentReports report;
    protected ExtentTest test;
    private ExtentHtmlReporter htmlReporter;
    protected LoginPage loginPage;
    protected Poll poll;
    protected Topic topic;


    protected Message message;

    @BeforeSuite
    public void setUpSuite() {
        report = new ExtentReports();
        String path = System.getProperty("user.dir") + "/test-output/report.html";
        htmlReporter = new ExtentHtmlReporter(path);
        htmlReporter.config().setReportName("Bitrix24 Automated Tests");
        report.attachReporter(htmlReporter);
        report.setSystemInfo("Enviroment", "QA");
        report.setSystemInfo("Browser", ConfigurationReader.getProperty("browser"));
    }

    @AfterSuite
    public void tearDownSuite() {
        report.flush();
    }


    @BeforeMethod
    public void setupMethod() {
        driver = Driver.getDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 10);
        // driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(ConfigurationReader.getProperty("url"));

        loginPage = new LoginPage();
        message = new Message();
        poll = new Poll();
        topic = new Topic();


    }

    @AfterMethod
    public void tearDownMethod(ITestResult iTestResult) throws IOException {
        if (iTestResult.getStatus() == ITestResult.FAILURE) {
            test.fail(iTestResult.getName());
            String screenShot = BrowserUtilities.getScreenshot(iTestResult.getName());
            test.addScreenCaptureFromPath(screenShot);
        }
        Driver.closeDriver();

    }
}
