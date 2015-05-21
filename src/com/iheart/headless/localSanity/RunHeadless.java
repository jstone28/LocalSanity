package com.iheart.headless.localSanity;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.junit.rules.TestName;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.iheart.selenium.localSanity.Page;
import com.iheart.selenium.localSanity.Z100HomePage;

public class RunHeadless {

	private WebDriver driver;
	
	protected static DesiredCapabilities dCaps;
	
	Z100HomePage homePage;
	
	 
	final String URL = "http://www.Z100.com";
	
	@Rule public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {

		
		
		// System.setProperty("phantomjs.binary.path", "C:\\Users\\1111128\\workspace\\lib\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe");
		dCaps = new DesiredCapabilities();
		dCaps.setJavascriptEnabled(true);
		dCaps.setCapability("takesScreenshot", true);
		
		driver = new PhantomJSDriver(dCaps);
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		 Page.setDriver (driver);
	     homePage = PageFactory.initElements(driver, Z100HomePage.class);
	        
	      Page.getErrors().delete(0, Page.getErrors().length());
	}

	
	
	 @Test
	 public void testAL_485_listenLive() throws Exception
	 {   driver.get(URL + "/");
	 	System.out.println("test method:" +  name.getMethodName() );
	 	try{
	 		homePage.testAL_485_listenLive();
	 	}catch(Exception e)
	 	{
	 		handleException(e);
	 	}  	
	 	System.out.println(name.getMethodName() + " is Done.");
	 }
	

	@After
	public void tearDown() throws Exception {
		Page.takeScreenshot(driver, name.getMethodName());
		driver.quit();
		if (Page.getErrors().length() > 0)
			 fail(Page.getErrors().toString());
		
	}
	
	
	 private void handleException(Exception e)
	    {   Page.getErrors().append("Exception is thrown.");
	        e.printStackTrace();
         try{
	    	   Page.takeScreenshot(driver, name.getMethodName());
         }catch(Exception eX)
         {
         	
         }
	    }
	    
	   
}
