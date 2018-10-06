package com.Axon.Tiki.BuiltIn;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import com.Axon.Tiki.TikiTest.*;

import java.util.concurrent.TimeUnit;

import static com.Axon.Tiki.TikiTest.AbstractPageTest.*;

public class BuiltInGetter
{
    public static boolean isElementPresentBySelector(String argSelector)
    {
        boolean isXpath = argSelector.contains("//");
        WebElement element;
        try
        {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            if (isXpath)
            {
                element = getDriver().findElement(By.xpath(argSelector));
            }
            else
            {
                element = getDriver().findElement(By.cssSelector(argSelector));
            }
            boolean isPresent = element.isDisplayed();
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            return isPresent;
        }
        catch (NoSuchElementException e)
        {
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            return false;
        }
    }

    public static boolean isElementPresent(WebElement element)
    {
        try
        {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            boolean isPresent = element.isDisplayed();
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            return isPresent;
        }
        catch (NoSuchElementException e)
        {
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            return false;
        }
        catch (NullPointerException npe)
        {
            getDriver().manage()
                    .timeouts()
                    .implicitlyWait(getTimeOut(), TimeUnit.SECONDS);
            return false;
        }
    }


}
