package com.Axon.Tiki.TikiTest;

import Interface.IAbstractPageObject;
import Interface.IHomePage;
import com.Axon.Tiki.Utilities.Constants;
import Utilities.Log;
import com.Axon.Tiki.Utilities.ExtentManager;
import com.Axon.Tiki.Utilities.POconfig;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.Status;
import org.testng.internal.thread.ThreadTimeoutException;
import pararell.LocalDriverManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static Utilities.Log.getLogObj;

public class AbstractPageTest {

    private static POconfig config;
    private static String browser;
    private static Log logger = new Log();
    private static Map<String, StringBuffer> mapVerificationErrors = new HashMap<String, StringBuffer>();
    private static Map<Long, String> mapThreadTest = new HashMap<Long, String>();
    private static ExtentReports extentReport;
    private static Map<Long, ExtentTest> mapExtentTest = new HashMap<Long, ExtentTest>();
    private static String projectDir = System.getProperty("user.dir");
    private static String className = "AbstractBaseTest";


    public static String getBrowser()
    {
        String strBrowser = config.getProperty("browser");
        return strBrowser;
    }

    public static Log getLogObj()
    {
        return logger;
    }

    public static int getTimeOut()
    {
        String strTimeout = config.getProperty("globalTimeOut");
        return Integer.parseInt(strTimeout);
    }

    public static long getCurrentThreadId()
    {
        return Thread.currentThread().getId();
    }

    public static ExtentTest getTestReport()
    {
        return mapExtentTest.get(getCurrentThreadId());
    }

    public  static StringBuffer getVerificationErrors()
    {
        String testMethodName = mapThreadTest.get(getCurrentThreadId());
        return mapVerificationErrors.get(testMethodName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPageObject(Class<T> pageInterfaceToProxy)
    {
        T page = null;
        try
        {
            Class<T> pageClassToProxy = config.getPageObjectImplementation(getDriver(), pageInterfaceToProxy);
            page = PageFactory.initElements(getDriver(), pageClassToProxy);
        }
        catch (Exception e)
        {
            if (e.getCause() != null && e.getCause() instanceof InvocationTargetException)
            {
                InvocationTargetException exc = (InvocationTargetException)e.getCause();
                if (exc.getTargetException() != null)
                {
                    Assert.fail(exc.getTargetException().getMessage());
                }
                else
                {
                    Assert.fail(exc.getMessage());
                }
            }
            String testMethodName =  mapThreadTest.get(getCurrentThreadId());
            StringBuffer verificationErrors = mapVerificationErrors.get(testMethodName);
            verificationErrors.append(e.toString());
            String verificationErrorString = verificationErrors.toString();
            if (!"".equals(verificationErrorString))
            {
                Assert.fail(verificationErrorString);
            }
        }
        return page;
    }

    public void setEnvironment(String strEnvironment)
    {
        getLogObj().info(className + " - Step - setEnvironment " + strEnvironment);
        getTestReport().log(Status.INFO, className + " - Step - setEnvironment " + strEnvironment);
        if (strEnvironment.equals("intvn"))
        {
            getDriver().get("https://ofwi-int-vn-dn.teledata.local:8078/td-web/login");
        }
        else
        {
            getDriver().get("https://" + strEnvironment.toLowerCase() +  ".teledata.ch");
        }
        String resolution = getProperty("resolution");
        if (resolution.equals("0"))
        {
            getDriver().manage().window().maximize();
        }
        else if (!resolution.equals("1") & !resolution.equals("0"))
        {
            String[] strparts = resolution.toLowerCase().split(Constants.CHARACTER.COMMA);
            int windowWidth = Integer.parseInt(strparts[0]); int windowHeight = Integer.parseInt(strparts[1]);
            Dimension dimension = new Dimension(windowWidth, windowHeight);
            getDriver().manage().window().setSize(dimension);
        }
    }

    public IHomePage getHomePage() throws Exception
    {
        IHomePage homepage = getPageObject(IHomePage.class); return homepage;
    }

    public static IAbstractPageObject getAbstractPageObject() throws Exception
    {
        IAbstractPageObject abstractpageobject = getPageObject(IAbstractPageObject.class); return abstractpageobject;
    }

    public static WebDriver getDriver()
    {
        return LocalDriverManager.getDriver();
    }

    public static String getProperty(String properties)
    {
        try
        {
            config = new POconfig();
        }
        catch (IOException e)
        {
            getLogObj().warn("Error occurred: " + e.getMessage().toString());
        }
        return config.getProperty(properties);
    }

    public static void failVerificationPoint(AssertionError e)
    {
        try
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String testStepName = Thread.currentThread().getStackTrace()[1].getMethodName();
            String fullStackTrace = ExceptionUtils.getStackTrace(e);
            getTestReport().log(Status.FAIL, "<pre>" + fullStackTrace + "</pre>");
            String currentBrowser = getBrowser();
            if (!"7".equals(currentBrowser))
            {
                String fileScreenshotName = projectDir + "\\logs\\failedScreenshots\\" + timeStamp + testStepName + ".jpeg";
                File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(fileScreenshotName), true);
                getTestReport().log(Status.INFO, "Snapshot for the failed point below: " + getTestReport().addScreenCaptureFromPath(fileScreenshotName));
            }
            else
            {
                getTestReport().log(Status.INFO, "NOTE! The snapshot for the failed point is not available for HTMLUnitDriver");
            }
        }
        catch (IOException ioe)
        {
            getLogObj().warn(ioe.toString());
        }
    }

    public void setBrowser(String browser)
    {
        WebDriver driver = null;
        if ("0".equals(browser))
        {
            getLogObj().info(className + " - Step - setBrowser Firefox");
            getTestReport().log(Status.INFO, className + " - Step - setBrowser Firefox");
            FirefoxProfile firefoxProfile = new FirefoxProfile(new File(projectDir + "/src/Conf/firefox-profile"));
            firefoxProfile.setPreference("browser.download.folderList", 2);
            //firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + "\\logs\\downloadedCSV");
            //firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
            System.setProperty("webdriver.gecko.driver", projectDir + "/src/Conf/GeckoDriverServer/geckodriver.exe"); System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            FirefoxOptions option = new FirefoxOptions(); option.setProfile(firefoxProfile);
            driver = new FirefoxDriver(option);
            getTestReport().log(Status.INFO, className + "=== Step - setBrowser FIREFOX - LOCAL");
        }
        LocalDriverManager.setWebDriver(driver);
    }

    @BeforeTest(alwaysRun = true)
    public void initializeBrowserConfig() throws AWTException, InterruptedException, IOException
    {
        config = new POconfig();
        browser = getBrowser();
    }

    @BeforeMethod(alwaysRun = true)
    public void init(Method test) throws Exception
    {
        try {
            StringBuffer verificationErrors = new StringBuffer();
            verificationErrors.setLength(0);
            mapVerificationErrors.put(test.getName(), verificationErrors);
            mapThreadTest.put(Thread.currentThread().getId(), test.getName());
            extentReport = ExtentManager.getInstance(InetAddress.getLocalHost().getHostAddress());
            config = new POconfig();
            ExtentTest extentTest = extentReport.createTest(test.getName());
            extentTest.assignCategory(test.getDeclaringClass().getName());
            mapExtentTest.put(getCurrentThreadId(), extentTest);
            getLogObj().info(className + "=== INITIALIZATION === Step - init");
            getTestReport().log(Status.INFO, className + "=== INITIALIZATION === Step - init");
            setBrowser(browser);
            //get all page object
            getHomePage();getAbstractPageObject();
            logger.startTestCase(test.getName());
        } catch (UnreachableBrowserException ube) {
            getLogObj().warn(ube.toString());
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + ube.toString());
        } catch (StaleElementReferenceException sere) {
            getLogObj().warn(sere.toString());
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + sere.toString());
        } catch (SessionNotCreatedException snce) {
            getLogObj().warn(snce.toString());
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + snce.toString());
        } catch (NullPointerException ex) {
            getLogObj().warn(ex.toString());
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + ex.toString());
        } catch (WebDriverException wde) {
            getLogObj().warn(wde.toString());
            getVerificationErrors().append(Constants.CHARACTER.BREAK_TO_NEW_LINE + wde.toString());
        }

    }

    @AfterMethod
    public void destroy(Method test, ITestResult result) throws Exception
    {
        getLogObj().info(className + "=== FINALIZATION === Step - destroy");
        getTestReport().log(Status.INFO, className + "=== FINALIZATION === Step - destroy");
        // Giving log for starting the test
        logger.endTestCase();
        ExtentTest testReport = getTestReport();
        // Set test result
        if (result.getStatus() == ITestResult.FAILURE)
        {
            StringBuilder msg = new StringBuilder("");
            Throwable t = result.getThrowable();
            if (t != null)
            {
                String nl = System.getProperty("line.separator");
                msg.append(nl); msg.append("    "); msg.append(t.toString());
                // If it's not a thread timeout, include the stack trace too
                if (!(t instanceof ThreadTimeoutException))
                {
                    for (StackTraceElement e : t.getStackTrace())
                    {
                        msg.append(nl); msg.append("    "); msg.append(e.toString());
                    }
                    testReport .log(Status.FAIL, "Test case result: " + "<font color='#c64444'><b>FAILED - Due to the " + msg + "</font>");
                }
            }
        }
        else if (result.getStatus() == ITestResult.SUCCESS)
        {
            testReport.log(Status.PASS, "Test case result: " + "<font color='#32cd32'><b>PASSED</font>");
        }
        else if (result.getStatus() == ITestResult.SKIP)
        {
            String exception = getVerificationErrors().toString();
            testReport.log(Status.WARNING, "Test case result: " + "<font color='#1e90ff'><b>SKIPPED - Due to the " + exception + "</font>");
        }
        else
        {
            testReport.log(Status.WARNING, "Test case result: " + "<font color='#222'><b>UNKNOWN</font>");
        }
        // Writing the HTML report
        extentReport.flush();
        try
        {
            //getDriver().get("http://" + InetAddress.getLocalHost().getHostAddress() + ":4444/lifecycle-manager?action=shutdown");
            Thread.sleep(1000); getDriver().close();
            Thread.sleep(1000); getDriver().quit();
        }
        catch (UnreachableBrowserException ube)
        {
            getLogObj().warn(ube.toString());
        }
        catch (ProcessStillAliveException psae)
        {
            getLogObj().warn(psae.toString());
        }
        catch (WebDriverException wde)
        {
            getLogObj().warn(wde.toString());
        }
        StringBuffer verificationErrors = new StringBuffer(); verificationErrors.setLength(0);
        mapVerificationErrors.put(test.getName(), verificationErrors); mapThreadTest.put(Thread.currentThread().getId(), test.getName());


    }

    @AfterClass(alwaysRun = true)
    public void showHTMLReport()
    {
        Runtime rt = Runtime.getRuntime();
        try
        {
            String command = "start firefox D:\\projects\\td-web-testing\\logs\\" + ExtentManager.getFileName();
            rt.exec(new String[]{"cmd.exe", "/c", command});
        }
        catch (IOException e)
        {
            getLogObj().warn(e.toString());
        }
    }

    public static String getEnvironment()
    {
        return config.getProperty("environment");
    }

    private class ProcessStillAliveException extends RuntimeException
    {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
        /**
         * Constructor
         *
         */
        public ProcessStillAliveException()
        {
            super();
        }
        /**
         * Constructor
         *
         * @param message
         * @param cause
         */
        public ProcessStillAliveException(String message, Throwable cause)
        {
            super(message, cause);
        }
        /**
         * Constructor
         *
         * @param message
         */
        public ProcessStillAliveException(String message)
        {
            super(message);
        }
        /**
         * Constructor
         *
         * @param cause
         */
        public ProcessStillAliveException(Throwable cause)
        {
            super(cause);
        }

    }
}
