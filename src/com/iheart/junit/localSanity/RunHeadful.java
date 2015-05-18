package com.iheart.junit.localSanity;


import com.iheart.selenium.localSanity.*;

import static org.junit.Assert.*; 

import org.junit.Test; 
import org.junit.Ignore; 
import org.junit.Before; 
import org.junit.After; 
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class RunHeadful {

	 WebDriver driver;
		
	 Z100HomePage homePage;
		
		
		String browser = "firefox";
		
		static String userCity = "";
		
		 
		final String URL = "http://www.Z100.com";
		
		@Rule public TestName name = new TestName();
		
		
		@Before
	    public void init() {
	        driver = Utils.launchBrowser(URL, browser);
	        Page.setDriver (driver);
	        homePage = PageFactory.initElements(driver, Z100HomePage.class);
	        
	        Page.getErrors().delete(0, Page.getErrors().length());
	    }

		
		
		 @Ignore("Skip this")
		 public void listOutLinks() throws Exception
		 {   
		 	System.out.println("test method:" +  name.getMethodName() );
		 	try{
		 		homePage.listOutLinks();
		 	}catch(Exception e)
		 	{
		 		handleException(e);
		 	}  	
		 	System.out.println(name.getMethodName() + " is Done.");
		 }
		
		 @Test
		 public void testAL_485_listenLive() throws Exception
		 {   
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
	    public void tearDown() throws Exception{
	    	driver.quit(); 
	    	if (Page.getErrors().length() > 0)
				 fail(Page.getErrors().toString());
	    	closeBrowserSession();
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
	    
	    public void closeBrowserSession() throws Exception 
		  { 
		    	
			 // Runtime.getRuntime().exec("taskkill /F /IM chrome.exe"); 
			 // Runtime.getRuntime().exec("taskkill /F /IM iexplorer.exe"); 
			  Runtime.getRuntime().exec("taskkill /F /IM firefox.exe"); 
		  }
}
