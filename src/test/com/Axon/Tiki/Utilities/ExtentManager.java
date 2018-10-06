/*
 * ExtentManager.java
 *
 * Copyright by CRIF AG
 * Z�rich
 * All rights reserved.
 */
package com.Axon.Tiki.Utilities;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import static com.Axon.Tiki.TikiTest.AbstractPageTest.*;
import static com.Axon.Tiki.TikiTest.AbstractPageTest.getBrowser;
import static com.Axon.Tiki.TikiTest.AbstractPageTest.getBrowser;

import com.aventstack.extentreports.reporter.*;
/**
 *
 *
 * @author dtanh
 * @version $Revision: 1.5 $
 */
public class ExtentManager
{
    private static ExtentReports extent;
    private static String fileName;

    /**
     * Method description
     * @param ip
     *
     * @return extent
     */
    @SuppressWarnings("unchecked")
    public static ExtentReports getInstance(String ip)
    {
        if (extent == null)
        {
            String ipRunning = ip;
            @SuppressWarnings("rawtypes")
            Map sysInfo = new HashMap();
            String browser = getBrowser();
            String browserName;
            if ("0".equals(browser))
            {
                browserName = "Firefox";
            }
            else if ("6".equals(browser))
            {
                browserName = "Chrome";
            }
            else if ("1".equals(browser))
            {
                browserName = "Internet Explorer";
            }
            else
            {
                browserName = "Remote Web Driver";
                if (StringUtils.containsIgnoreCase(browser, "-I-"))
                {
                    ipRunning = getProperty("instanceIpAddress");
                }
            }
            sysInfo.put("Browser", browserName);
            sysInfo.put("Environment", getEnvironment().toUpperCase());
            fileName = "ExtentTestReport_" + ipRunning + ".html";
            extent = new ExtentReports();
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("logs/" + fileName);
            extent.attachReporter(htmlReporter);
            // optional

           /* extent.config()
                .documentTitle("TD-WEB Automation Report")
                .reportName("Automation Report")
                .reportHeadline("- TR Regression Test");*/

            for (String s : Reporter.getOutput())
            {
                extent.setTestRunnerOutput(s);
            }

            //extent.addSystemInfo(sysInfo);
        }
        return extent;
    }

    /**
     * Method description
     *
     * @param millis
     * @return calendar.getTime();
     */
    public Date getTime(final long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    /**
     * Gets the fileName
     *
     * @return Returns the fileName
     */
    public static String getFileName()
    {
        return fileName;
    }

}

/*
 * Changes:
 * $Log: ExtentManager.java,v $
 * Revision 1.5  2017/11/14 10:15:26  trk
 * Task 844 - Fix losting HTML report in case run Grib
 *
 * Revision 1.4  2017/11/13 08:49:14  trk
 * Task 844 - Fix losting HTML report in case run Grib
 *
 * Revision 1.3  2017/06/08 10:51:54  nah
 * *** empty log message ***
 *
 * Revision 1.2  2016/09/08 08:55:31  nah
 * Fix for CRIF checkstyle: Changed from OFWI to * Copyright by CRIF AG
 *
 * Revision 1.1  2015/12/18 08:58:15  nah
 * *** empty log message ***
 *
 * Revision 1.2  2015/11/17 06:31:33  nah
 * *** empty log message ***
 *
 * Revision 1.1  2015/11/12 04:23:55  nah
 * *** empty log message ***
 *
 */