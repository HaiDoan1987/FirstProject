package Utilities;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class POconfig  {
    private static Properties prop = new Properties();

    public POconfig() throws IOException
    {
        InputStream in = getClass().getResourceAsStream("/td-web-testing/testconfig.properties");
        try
        {
            prop.load(in);
        }
        finally
        {
            in.close();
        }
    }

    public <T> Class getPageObjectImplementation(WebDriver driver, Class<T> pageInterfaceToProxy) throws Exception
    {
        ClassLoader classLoader = POconfig.class.getClassLoader();
        Class impl = classLoader.loadClass(prop.getProperty(pageInterfaceToProxy.getSimpleName()));
        return impl;
    }

    public String getProperty(String propertyName)
    {
        return prop.getProperty(propertyName);
    }

    public static int getTimeOut()
    {
        String strTimeout = prop.getProperty("globalTimeOut");
        int glbTimeOut = Integer.parseInt(strTimeout);
        return glbTimeOut;
    }
}
