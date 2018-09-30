package BuiltIn;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import
import java.util.concurrent.TimeUnit;

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
            getTestReport().log(LogStatus.INFO, "VP - verifyElementExist " + element.toString());
            elementExistStatus = BuiltInGetter.isElementPresent(element);
        }
        catch (NoSuchElementException nse)
        {
            getLogObj().warn(nse.toString());
            getTestReport().log(LogStatus.WARNING, nse.toString());
        }
        catch (NullPointerException npe)
        {
            getLogObj().warn(npe.toString());
            getTestReport().log(LogStatus.WARNING, npe.toString());
        }
        catch (TimeoutException te)
        {
            getLogObj().warn(te.toString());
            getTestReport().log(LogStatus.WARNING, te.toString());
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
            getTestReport().log(LogStatus.PASS, "Element existed");
        }
        else
        {
            getTestReport().log(LogStatus.FAIL, "The expected result that the element should be EXISTED but it was not.");
        }
        return testResult;
    }
}
