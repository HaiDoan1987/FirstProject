package test;

import org.testng.annotations.BeforeTest;

import java.awt.*;
import java.io.IOException;

public class AbstractPageTest  {



    @BeforeTest(alwaysRun = true)
    public void initializeBrowserConfig() throws AWTException, InterruptedException, IOException
    {
       // config = new POConfig();
        //browser = getBrowser();
    }
}
