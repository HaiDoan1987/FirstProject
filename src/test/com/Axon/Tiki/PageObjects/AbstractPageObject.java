package com.Axon.Tiki.PageObjects;

import Interface.IAbstractPageObject;
import Utilities.Log;
import com.Axon.Tiki.TikiTest.AbstractPageTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.Axon.Tiki.TikiTest.AbstractPageTest.getTimeOut;


public class AbstractPageObject implements IAbstractPageObject {

    private String sepeMarkReport = "\"";
    private static Log logger =  new Log();

    private WebDriver driver = null;
    private static String className = Thread.currentThread().getStackTrace()[1].getClassName();

    public AbstractPageObject(WebDriver driver)
    {
        if (driver == null)
        {
            getLogObj().warn("===WARNING=== WebDriver parameter is not allowed to be null!");
            throw new IllegalStateException("WebDriver parameter is not allowed to be null!");
        }
        this.driver = AbstractPageTest.getDriver();
    }

    public static Log getLogObj()
    {
        return logger;
    }

    public static WebDriver getDriver()
    {
        return AbstractPageTest.getDriver();
    }

    public void sendKeys(WebElement element, String value, boolean clearflag) throws Exception
    {
        try
        {
            waitForElement(element);
            getLogObj().info(className + " - Step - Clear the input field then sendKeys " + sepeMarkReport + value + sepeMarkReport + " into the web element " + sepeMarkReport + element + sepeMarkReport);
            AbstractPageTest.getTestReport().log(Status.INFO, className + " - Step - Clear the input field then sendKeys " + sepeMarkReport + value + sepeMarkReport + " into element " + sepeMarkReport + element + sepeMarkReport);
            if (clearflag)
            {
                element.clear();
            }
            element.sendKeys(value);
        }
        catch (Exception ex)
        {
            failStepPoint(ex);
            throw ex;
        }
    }

    public void waitForElement(WebElement element) throws Exception
    {
        if (element == null)
        {
            NoSuchElementException nse = new NoSuchElementException("Cannot find webElement. Throw NoSuchElementException.");
            throw nse;
        }
        try
        {
            getLogObj().info(className + " - Step - waitForElement");
            AbstractPageTest.getTestReport().log(Status.INFO, className + " - Step - waitForElement");
            long startTime = System.currentTimeMillis();
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            int glbTimeOut = getTimeOut();
            getLogObj().info(className + " - Step - Finished waiting for element " + element);
            AbstractPageTest.getTestReport().log(Status.INFO, className + " - Step - Finished waiting for element " + element);
            WebDriverWait wait = new WebDriverWait(driver, glbTimeOut);
            wait.until(ExpectedConditions.visibilityOf(element));
            long endTime = System.currentTimeMillis();
            long elapsedTime = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
            getLogObj().info(className + " - Step - Finished waiting for element after " + elapsedTime + " seconds");
            AbstractPageTest.getTestReport().log(Status.INFO, className + " - Step - Finished waiting for element after " + elapsedTime + " seconds");
            driver.manage().timeouts().implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
        }
        catch (StaleElementReferenceException sele)
        {
            getLogObj().warn(sele.toString());
            AbstractPageTest.getTestReport().log(Status.WARNING, sele.toString());
        }
        catch (InvalidElementStateException iese)
        {
            getLogObj().warn(iese.toString());
            AbstractPageTest.getTestReport().log(Status.WARNING, iese.toString());
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public static void failStepPoint(Exception e) throws Exception
    {
        try
        {
            getDriver().manage().timeouts().implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance()
                    .getTime());
            String testStepName = Thread.currentThread().getStackTrace()[1].getMethodName();
            String fullStackTrace = ExceptionUtils.getStackTrace(e);
            AbstractPageTest.getTestReport().log(Status.FAIL,
                    "<pre>" + fullStackTrace + "</pre>");
            String fileScreenshotName = "D:\\projects\\td-web-testing\\logs\\failedScreenshots\\"
                    + timeStamp + testStepName + ".jpeg";
            File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(fileScreenshotName), true);
            AbstractPageTest.getTestReport()
                    .log(Status.INFO, "Snapshot for the failed point below: "
                            + AbstractPageTest.getTestReport().addScreenCaptureFromPath("D:\\projects\\td-web-testing\\logs\\failedScreenshots\\"
                            + timeStamp + testStepName + ".jpeg"));
        }
        catch (IOException ioe)
        {
            getLogObj().warn(ioe.toString());
        }
        finally
        {
            Assert.fail("Error happens - The exception is thrown at the end action " + AbstractPageTest.getVerificationErrors().toString());
        }
    }

}
