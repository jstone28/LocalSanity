package com.iheart.selenium.localSanity;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public abstract class Page {

	
	
  
	//FACE BOOK Signup info
	public final String FACEBOOKemail = "iheartRadio.tribecca@gmail.com";
	public final String _PASSWORD = "iheart001";

   public static WebDriver driver;
   public static final String screenshot_folder="screenshots";
   public static StringBuffer errors = new StringBuffer(); 
   
   public static String browser = "";
   
   public Page()
   {
	   
   }
	
   public Page(WebDriver _driver)
   {
	   driver = _driver;
   }

   public static void takeScreenshot_INPROGRESS(WebDriver driver, String testMethod) throws Exception 
   {      
		  // Creating new directory in Java, if it doesn't exists
	       File directory = new File(screenshot_folder);
	       
	       if (!directory.exists()) 
	       {
	           System.out.println("Directory not exists, creating now");
	
	           boolean success = directory.mkdir();
	           if (success) {
	               System.out.printf("Successfully created new directory : %s%n", screenshot_folder);
	           } else {
	               System.out.printf("Failed to create new directory: %s%n", screenshot_folder);
	           }
	       }
	       
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
   			Date date = new Date();
   			//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	       String screenshotName = testMethod + dateFormat.format(date) + ".png";
	       System.out.println("See screenshotName:" + screenshotName);
           File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in d drive with name "screenshot.png"
           FileUtils.copyFile(scrFile, new File( screenshotName));
       
           System.out.println("Screenshot is taken.");
   }
   
   
   public static void takeScreenshot(WebDriver driver, String testMethod) throws Exception 
   {      
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
   			Date date = new Date();
   			//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	       String screenshotName = testMethod + dateFormat.format(date) + ".png";
	       System.out.println("See screenshotName:" + screenshotName);
           File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in d drive with name "screenshot.png"
           FileUtils.copyFile(scrFile, new File(screenshotName));
           System.out.println("Screenshot is taken.");
   }
   
   public static void setDriver(WebDriver _driver)
   {
	   driver = _driver;
   }
   
   public static WebDriver getDriver()
   {
	   return driver;
   }
   
  
	

   public String  switchWindow()
	{
		//Switch to new tab where the sign-up is
		String winHandleBefore = driver.getWindowHandle();
		//Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}	
		return winHandleBefore;
	}
	
	public void goToPreviousWindow(WebDriver  currentDriver, String winHandleBefore)
	{
	    currentDriver.close();
		//Switch back to original browser (first window)
	    driver.switchTo().window(winHandleBefore);
	}
	
	public String getCurrentDateString()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public String getCurrentDateInMilli()
	{
		Date date = new Date();
		return date.getTime() + "";
	}
   
	
	
	public void handleError(String msg, String methodName) 
	{
		errors.append(msg);
		try{
		   takeScreenshot( driver,  methodName);
		}catch(Exception e)
		{
			System.out.println("Exception is thrown taking screenshot.");
		}
	}
	
	public void doubleClick(WebElement element)
	{
		Actions action = new Actions(driver);
		//Double click
		action.doubleClick(element).perform();

	}
	
	
	public static StringBuffer getErrors()
	{
		return errors;
	}
	
	public static void setBrowser(String _browser)
	{
		browser = _browser;
	}
	
	
	
}
