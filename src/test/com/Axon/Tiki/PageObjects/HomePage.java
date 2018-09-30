package com.Axon.Tiki.PageObjects;

import Interface.IHomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPageObject implements IHomePage {

    public HomePage(WebDriver driver)
    {
        super(driver);
    }

    public WebElement getLogoTiki() {
        return logoTiki;
    }

    @FindBy(css = "i[class='tikicon icon-logo-tiki']")
    private WebElement logoTiki;


}
