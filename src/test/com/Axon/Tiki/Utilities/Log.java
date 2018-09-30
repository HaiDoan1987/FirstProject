package Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static Logger logObj = LogManager.getLogger();

    public static Logger getLogObj()
    {
        return logObj;
    }
    public void info(String message)
    {
        logObj.info(message);
    }
    public void warn(String message)
    {
        logObj.warn(message);
    }

    public void error(String message)
    {
        logObj.error(message);
    }
    public void fatal(String message)
    {
        logObj.fatal(message);
    }
    public void debug(String message)
    {
        logObj.debug(message);
    }

    public void startTestCase(String strTestCaseName)
    {
        logObj.info("$$$$$$$$$$$$$$$$$$   " + strTestCaseName + "   $$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

    public void endTestCase()
    {
        logObj.info("XXXXXXXXXXXXXXXXXXXXXX   " + "-E---N---D-" + "   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        logObj.info("\n");
    }
}
