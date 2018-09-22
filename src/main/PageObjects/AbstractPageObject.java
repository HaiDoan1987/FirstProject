package PageObjects;

import Interface.IAbstractPageObject;
import Utilities.Log;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.AbstractPageTest;
import java.util.concurrent.TimeUnit;

import static Utilities.POconfig.getTimeOut;

public class AbstractPageObject implements IAbstractPageObject {

    private  AbstractPageTest AbstractBaseTest = new AbstractBaseTest();
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
        this.driver = driver;
    }

    public static Log getLogObj()
    {
        return logger;
    }

    public static WebDriver getDriver()
    {
        return AbstractBaseTest.getDriver();
    }

    public void sendKeys(WebElement element, String value, boolean clearflag) throws Exception
    {
        try
        {
            waitForElement(element);
            getLogObj().info(className + " - Step - Clear the input field then sendKeys " + sepeMarkReport + value + sepeMarkReport + " into the web element " + sepeMarkReport + element + sepeMarkReport);
            AbstractBaseTest.getTestReport().log(Status.INFO, className + " - Step - Clear the input field then sendKeys " + sepeMarkReport + value + sepeMarkReport + " into element " + sepeMarkReport + element + sepeMarkReport);
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
            AbstractBaseTest.getTestReport().log(Status.INFO, className + " - Step - waitForElement");
            long startTime = System.currentTimeMillis();
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            int glbTimeOut = getTimeOut();
            getLogObj().info(className + " - Step - Finished waiting for element " + element);
            AbstractBaseTest.getTestReport().log(Status.INFO, className + " - Step - Finished waiting for element " + element);
            WebDriverWait wait = new WebDriverWait(driver, glbTimeOut);
            wait.until(ExpectedConditions.visibilityOf(element));
            long endTime = System.currentTimeMillis();
            long elapsedTime = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
            getLogObj().info(className + " - Step - Finished waiting for element after " + elapsedTime + stringSeconds);
            AbstractBaseTest.getTestReport().log(Status.INFO, className + " - Step - Finished waiting for element after " + elapsedTime + stringSeconds);
            driver.manage().timeouts().implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
        }
        catch (StaleElementReferenceException sele)
        {
            getLogObj().warn(sele.toString());
            AbstractBaseTest.getTestReport().log(Status.WARNING, sele.toString());
        }
        catch (InvalidElementStateException iese)
        {
            getLogObj().warn(iese.toString());
            AbstractBaseTest.getTestReport().log(Status.WARNING, iese.toString());
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
