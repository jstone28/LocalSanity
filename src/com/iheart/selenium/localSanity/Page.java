package com.iheart.selenium.localSanity;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  

import org.apache.http.client.HttpClient;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.httpclient.methods.GetMethod; 

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

	
   public static WebDriver driver;
   public static final String screenshot_folder="screenshots";
   public static StringBuffer errors = new StringBuffer(); 
   
   public static String browser = "";
   
   public static String url;
   
   public Page()
   {
	   
   }
	
   public Page(WebDriver _driver)
   {
	   driver = _driver;
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
   
	
	public void goThroughLinks() throws Exception
	{  
		String href ="";
    	String linkText ="";//if it is an image, put image src here
    	
		//for mobile site, click on sandwich to get all the links there
		if (isMobileSite(Page.getURL()))
			driver.findElement(By.cssSelector("[title='Menu']")).click();
		
		int statusCode = -1;
		List<BadLink> badLinks = new ArrayList<BadLink>();
		List<WebElement> links = driver.findElements(By.tagName("a"));  
		System.out.println("Total links: " + links.size());
		
				
        for (WebElement link: links)
        {   href ="";
            linkText ="";
        	//write all the links to a excel FILE
        	//Aren't null link and empty link suspicious?
        	try{
        		href  =	link.getAttribute("href").trim();
        		linkText = link.getText();
        		
        	}catch(Exception e)
        	{
        		System.out.println("Null href!");
        		
        	}
        	
			if (href != null && !href.equals("") && !isAdLink(href) &&
			     (!isSocial(href) && !isVoid(href)  && !isMobileNative(href)))
			{	
				
				System.out.println("See link: " +  href );
				try{
			    	statusCode= getResponseCode(href); 
				}catch(Exception e)
				{
					//Only take care image src for bad links
					
					System.out.println("eXCEPTON IS THROWN FOR HREF/STATUS:" + href + "--------" + link.getText() );
					
					badLinks.add(new BadLink(linkText, href, -2)); //status code is not available
				}
				//System.out.println("HREF/STATUS:" + href + "/" + status );
				if (statusCode != 200 && statusCode != 302)
				{	
				    if (linkText.equals(""))
				    {	
				    	linkText = link.getAttribute("src");
					    /*	
		        		linkText = link.getAttribute("innerHTML");
		        	    if (linkText.contains("<img"))
		        	    	linkText = link.getAttribute("src");
		        	    */	
				    }	
					
					System.out.println("HREF/STATUS:" + href + "-----" + linkText + "-------" +  statusCode );
					badLinks.add(new BadLink(linkText, href , statusCode));
				}
			}		
			
        
        }  //for()
  
        //output bad link to a file 
        
        System.out.println("Bad links/statusCode:");
        for (BadLink link: badLinks)
        	System.out.println(link.getUrl() + "------" + link.getStatusCode());
        
        ExcelUtility.writeToExcel(badLinks);
	}
	
	private boolean isSocial(String url)
	{
		return (url.contains("facebook.com") || url.contains("twitter.com") || url.contains("instagram.com") || url.contains("plus.google.com"));
	}
	
	private boolean isVoid(String url)
	{
		return url.contains("javascript:void(null)");
	}
	
	private boolean isMobileNative(String url)
	{   //if (url.contains("tel:") || url.contains("sms:"))
		 //  System.out.println("Caught mobile phone link:" + url);
		return url.contains("tel:") || url.contains("sms:") ;
	}
	
	private int getResponseCode(String urlString) throws MalformedURLException, IOException {         
	    URL u = new URL(urlString);  
	    HttpURLConnection huc = (HttpURLConnection) u.openConnection();  
	    huc.setRequestMethod("GET");  
	    huc.connect();  
	    System.out.println("response:" + huc.getResponseCode());
	    return huc.getResponseCode();  
	}
	/*
	private int getResponseCodeViaHttpClient(String href) throws MalformedURLException, IOException {         
		HttpClient client = new HttpClientBuilder();
		
		GetMethod getMethod = new GetMethod(href);
		int res = client.executeMethod(getMethod);
		return res;
	}
	*/
	
	public void handleError(String msg, String methodName) 
	{
		errors.append(msg);
		try{
		   Utils.takeScreenshot( driver,  methodName);
		}catch(Exception e)
		{
			System.out.println("Exception is thrown taking screenshot.");
		}
	}
	
	
	
	public static StringBuffer getErrors()
	{
		return errors;
	}
	
	public static void setBrowser(String _browser)
	{
		browser = _browser;
	}
	
	
	public static void setURL(String _url)
	{
		url = _url;
	}
	
	public static String getURL()
	{
		return url;
	}
	
	
	public boolean isMobileSite(String url)
	{
		return url.startsWith("http://m.") || url.startsWith("https://m.");
	}
	
	private boolean isAdLink(String url)
	{
		return url.contains("googleads.g.doubleclick.net");
	}
	
	/*
	 * See source:<a href="/main.html" title="Tribeca's Best Music" name="&amp;lid=station_logo&amp;lpos=poc:Homepage:header" onclick="s.tl(this,'o','poc:Homepage:header:station_logo');s_objectID='pocHomepage:header:station_logo';" itemprop="url">
	 * <img src="http://content.clearchannel.com/cc-common/mlib/15208/11/15208_1384545886.png?t=1" itemprop="logo"></a>
	 */
	/*
	private String parseImageSrc(String outerHTML)
	{
		
		String src = outerHTML.S
		
	}
  */
}
