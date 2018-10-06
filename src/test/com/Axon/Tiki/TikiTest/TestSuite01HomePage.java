package com.Axon.Tiki.TikiTest;

import com.Axon.Tiki.BuiltIn.BuiltInVerify;
import org.testng.annotations.Test;



public class TestSuite01HomePage extends AbstractPageTest {

    @Test
    public void testTR01VerifyLogoTikiExisted() throws Exception
    {
        BuiltInVerify.verifyElementExist(getHomePage().getLogoTiki());
    }
}
