package com.cybertek;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReadingTables {
	/*1) goto https://forms.zohopublic.com/murodil/report/Applicants/reportperma/DibkrcDh27GWoPQ9krhiTdlSN4_34rKc8ngubKgIMy8
	2) Create a HashMap
	3) change row number to 100, read all data on first page and put uniquID as a KEY 
	and Applicant info as a Value to a map. 

	applicants.put(29,"Amer, Sal-all@dsfdsf.com-554-434-4324-130000")

	4) Click on next page , repeat step 3
	5) Repeat step 4 for all pages 
	6) print count of items in a map. and assert it is matching
	with a number at the buttom
	============================*/
	WebDriver driver;
	
	@BeforeClass // runs once for all tests
	
	public void setUp() {
		System.out.println("Setting up WebDriver in BeforeClass...");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		System.out.println("Navigating to homepage in @BeforeMethod....");
		driver.get(
				"https://forms.zohopublic.com/murodil/report/Applicants/reportperma/DibkrcDh27GWoPQ9krhiTdlSN4_34rKc8ngubKgIMy8");
	}
	@Test
	public void ReadingInformation() throws InterruptedException {
		
		Select pageView= new Select (driver.findElement(By.id("recPerPage")));
		pageView.selectByVisibleText("100");
		Thread.sleep(3000);
		
		List<WebElement> info= driver.findElements(By.xpath("//table[@id='reportTab']//tbody/tr"));
		
		HashMap<String, String> appInfo = new HashMap<>();
		for (int i = 0; i < info.size(); i++) {
			String key=driver.findElement(By.xpath("//table[@id='reportTab']//tbody/tr["+(i+1)+"]/td[1]")).getText();
			String value=driver.findElement(By.xpath("//table[@id='reportTab']//tbody/tr["+(i+1)+"]/td[2]")).getText()+","
				     +driver.findElement(By.xpath("//table[@id='reportTab']//tbody/tr["+(i+1)+"]/td[3]")).getText()+","
				     +driver.findElement(By.xpath("//table[@id='reportTab']//tbody/tr["+(i+1)+"]/td[4]")).getText()+","
				     +driver.findElement(By.xpath("//table[@id='reportTab']//tbody/tr["+(i+1)+"]/td[5]")).getText()+",";
			
			appInfo.put(key, value);	
		}
		
		System.out.println(appInfo);
		
		String expected=driver.findElement(By.xpath("//span[@id='total']")).getText();
		String actual= String.valueOf(appInfo.size());
		Assert.assertEquals(actual, expected);
		
	}
	@AfterClass
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}
	
}
