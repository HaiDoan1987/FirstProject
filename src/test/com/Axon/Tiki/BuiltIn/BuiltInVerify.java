package com.Axon.Tiki.BuiltIn;

import com.Axon.Tiki.Utilities.Constants;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.util.concurrent.TimeUnit;

import static com.Axon.Tiki.TikiTest.AbstractPageTest.getDriver;
import com.Axon.Tiki.Utilities.Constants.*;
import static com.Axon.Tiki.TikiTest.AbstractPageTest.*;
import static com.Axon.Tiki.BuiltIn.BuiltInGetter.isElementPresentBySelector;
public class BuiltInVerify
{
    public static boolean verifyElementExist(WebElement element) throws Exception
    {
        boolean elementExistStatus = false;
        boolean testResult = false;
        getDriver().manage().timeouts().implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
        try
        {
            getLogObj().info("Start VP - verifyElementExist");
            getTestReport().log(Status.INFO, "Start VP - verifyElementExist");
            getAbstractPageObject().waitForElement(element);
            getLogObj().info("VP - verifyElementExist" + element.toString());
            getTestReport().log(Status.INFO, "VP - verifyElementExist " + element.toString());
            elementExistStatus = BuiltInGetter.isElementPresent(element);
        }
        catch (NoSuchElementException nse)
        {
            getLogObj().warn(nse.toString());
            getTestReport().log(Status.WARNING, nse.toString());
        }
        catch (NullPointerException npe)
        {
            getLogObj().warn(npe.toString());
            getTestReport().log(Status.WARNING, npe.toString());
        }
        catch (TimeoutException te)
        {
            getLogObj().warn(te.toString());
            getTestReport().log(Status.WARNING, te.toString());
        }
        try
        {
            Assert.assertTrue(elementExistStatus);
        }
        catch (AssertionError e)
        {
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + e.toString());
            getLogObj().warn(e.toString());
            failVerificationPoint(e);
        }
        testResult = elementExistStatus;
        if (testResult)
        {
            getTestReport().log(Status.PASS, "Element existed");
        }
        else
        {
            getTestReport().log(Status.FAIL, "The expected result that the element should be EXISTED but it was not.");
        }
        return testResult;
    }
}
