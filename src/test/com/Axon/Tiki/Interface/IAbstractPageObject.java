package Interface;

import org.openqa.selenium.WebElement;

public interface IAbstractPageObject  {
    void sendKeys(WebElement element, String value, boolean clearflag) throws Exception;

    void waitForElement(WebElement element) throws Exception;
}
