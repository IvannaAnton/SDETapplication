package com.cybertek;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SDETaplication {
	WebDriver driver;
	String firstName;
	String lastName;
	String gender;
	String dateOfBirth;
	String email;
	String phoneNumber;
	String city;
	String state;
	String country;
	int annualSalary;
	List<String> technologies;
	int yearsOfExperience;
	String education;
	String github;
	WebElement certifications;
	String additionalSkills;
	Faker data = new Faker();
	Random random = new Random();

	@BeforeClass // runs once for all tests
	public void setUp() {
		System.out.println("Setting up WebDriver in BeforeClass...");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
	}

	@BeforeMethod // runs before each @Test
	public void navigateToHomePage() {
		System.out.println("Navigating to homepage in @BeforeMethod....");
		driver.get(
				"https://forms.zohopublic.com/murodil/form/JobApplicationForm/formperma/kOqgtfkv1dMJ4Df6k4_mekBNfNLIconAHvfdIk3CJSQ");
		firstName = data.name().firstName();
		lastName = data.name().lastName();
		dateOfBirth = data.date().birthday().toString();
		email = "Ivanna.anton@aiesec.net";
		phoneNumber = data.phoneNumber().cellPhone().replace(".", "");
		city = data.address().cityName();
		state = data.address().stateAbbr();
		country = data.address().country();
		annualSalary = data.number().numberBetween(60000, 150000);
		technologies = new ArrayList<>();
		technologies.add("Java-" + data.number().numberBetween(1, 4));
		technologies.add("HTML-" + data.number().numberBetween(1, 4));
		technologies.add("Selenium WebDriver-" + data.number().numberBetween(1, 4));
		technologies.add("TestNG-" + data.number().numberBetween(1, 4));
		technologies.add("Git-" + data.number().numberBetween(1, 4));
		technologies.add("Maven-" + data.number().numberBetween(1, 4));
		technologies.add("JUnit-" + data.number().numberBetween(1, 4));
		technologies.add("Cucumber-" + data.number().numberBetween(1, 4));
		technologies.add("API Automation-" + data.number().numberBetween(1, 4));
		technologies.add("JDBC-" + data.number().numberBetween(1, 4));
		technologies.add("SQL-" + data.number().numberBetween(1, 4));

		yearsOfExperience = data.number().numberBetween(0, 11);
		education = data.number().numberBetween(1, 4) + "";
		github = "https://github.com/IvannaAnton/SDETapplication.git";
		
		additionalSkills = data.job().keySkills();

	}

	@Test
	public void submitFullApplication() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='Name_First']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@name='Name_Last']")).sendKeys(lastName);
		gender=setGender(data.number().numberBetween(1, 3));
		setDateOfBirth(dateOfBirth);
		driver.findElement(By.xpath("//input[@name='Email']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@name='countrycode']")).sendKeys(phoneNumber);
		driver.findElement(By.xpath("//input[@name='Address_City']")).sendKeys(city);
		driver.findElement(By.xpath("//input[@name='Address_Region']")).sendKeys(state);
		
		Select countryElem = new Select(driver.findElement(By.xpath("//select[@id='Address_Country']")));
		countryElem.selectByIndex(data.number().numberBetween(1, countryElem.getOptions().size()));
		WebElement option = countryElem.getFirstSelectedOption();
		String countrySelected = option.getText();
		
		driver.findElement(By.xpath("//input[@name='Number']")).sendKeys(String.valueOf(annualSalary) + Keys.TAB);
		verifySalaryCalculations(annualSalary);
		driver.findElement(By.xpath("//em[.=' Next ']")).click();

		// SECOND PAGE ACTIONS
		String skills=setSkillset(technologies);
		// years
		if (yearsOfExperience > 0) {
			driver.findElement(By.xpath("//a[@rating_value='" + yearsOfExperience + "']")).click();
			
		}
		// education
		Select educationList = new Select(driver.findElement(By.xpath("//select[@name='Dropdown']")));
		educationList.selectByIndex(data.number().numberBetween(1, educationList.getOptions().size()));
		WebElement edu=educationList.getFirstSelectedOption();
		education=edu.getText();
		// github
		driver.findElement(By.xpath("//input[@name='Website']")).sendKeys(github);

		selectCertifications();
		String certificete=certifications.getAttribute("value");
		driver.findElement(By.xpath("//textarea[@name='MultiLine']")).clear();
		driver.findElement(By.xpath("//textarea[@name='MultiLine']")).sendKeys(additionalSkills);
		// <em>Apply</em>
		driver.findElement(By.xpath("//em[.='Apply']")).click();
		Thread.sleep(3000);
		
		Iterator<String> iterator = driver.getWindowHandles().iterator();
		String subWindowHandler = null;
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		// * switch to popup window
		driver.switchTo().window(subWindowHandler);

		// varification()//name and last name
		String actual = driver.findElement(By.xpath("//label[@class='descFld']/div[1]")).getText();
		String expected = firstName + " " + lastName;
		assertTrue(actual.contains(expected));
		

		// ip adress
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[6]")).getText();
		expected = ipAdress();
		assertTrue(actual.contains(expected));
		
		//app id
		actual=driver.findElement(By.xpath("//label[@class='descFld']/div[8]")).getText();
		//expexted=

		// gender
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[9]")).getText();
		expected=gender;
		assertTrue(actual.contains(expected));

		//birthiday
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[10]")).getText();
		String[] pieces = dateOfBirth.split(" ");
		String birthDay = pieces[2] + "-" + pieces[1] + "-" + pieces[5];
		expected = birthDay;
		assertTrue(actual.contains(expected));
		
		//email
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[11]")).getText();
		expected=email;
		assertTrue(actual.contains(expected));
		
		//phone number
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[12]")).getText();
		expected=phoneNumber;
		assertTrue(actual.contains(expected));
		
		//address
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[13]")).getText();
		expected=city+", "+state+", "+countrySelected;
		assertTrue(actual.contains(expected));
		
		//salary
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[14]")).getText();
		expected=String.valueOf(annualSalary);
		assertTrue(actual.contains(expected));
		
		//skills
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[15]")).getText();
		expected=skills;
		assertTrue(actual.contains(expected));
		
		
		//years
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[16]")).getText();
		expected=String.valueOf(yearsOfExperience);
		assertTrue(actual.contains(expected));
		
		//education
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[17]")).getText();
    	expected=education;
	    assertTrue(actual.contains(expected));
		
		
		//git 
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[18]")).getText();
		expected=github;
		assertTrue(actual.contains(expected));
		
		//cercertifications
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[19]")).getText();
		expected=certificete;
		assertTrue(actual.contains(expected));
		
		//aditional
		actual = driver.findElement(By.xpath("//label[@class='descFld']/div[20]")).getText();
		expected=additionalSkills;
		assertTrue(actual.contains(expected));
		
		
		Thread.sleep(3000);
		
	}

	public String ipAdress() {

		WebDriver driver2 = driver;
		driver2.get("https://www.google.com");
		driver2.findElement(By.id("lst-ib")).sendKeys("what is my ip address?" + Keys.ENTER);
		String expectedIP = driver.findElement(By.cssSelector("div.pIpgAc.xyYs1c.XO51F.xsLG9d")).getText();
		driver.navigate().back();
		driver.navigate().back();
		return expectedIP;
	}

	public void selectCertifications() {
		int num = data.number().numberBetween(1, 4);
		int Certification;
		while ((Certification = data.number().numberBetween(1, 4)) == num) {
		}
		certifications=driver.findElement(By.xpath("//input[@id='Checkbox_" +Certification + "']"));
		certifications.click();

	}

	public String setSkillset(List<String> tech) {
      String skills="";
		for (String skill : tech) {
			String technology = skill.substring(0, skill.length() - 2);
			int rate = Integer.parseInt(skill.substring(skill.length() - 1));

			String level = "";

			switch (rate) {
			case 1:
				level = "Expert";
				break;
			case 2:
				level = "Proficient";
				break;
			case 3:
				level = "Beginner";
				break;
			default:
				fail(rate + " is not a valid level");
			}

			String xpath = "//input[@rowvalue='" + technology + "' and @columnvalue='" + level + "']";
			driver.findElement(By.xpath(xpath)).click();
			skills= technology+" : "+level;

		}
		return skills;

	}

	public void verifySalaryCalculations(int annual) {
		String monthly = driver.findElement(By.xpath("//input[@name='Formula']")).getAttribute("value");
		String weekly = driver.findElement(By.xpath("//input[@name='Formula1']")).getAttribute("value");
		String hourly = driver.findElement(By.xpath("//input[@name='Formula2']")).getAttribute("value");

		System.out.println(monthly);
		System.out.println(weekly);
		System.out.println(hourly);

		DecimalFormat formatter = new DecimalFormat("#.##");

		assertEquals(Double.parseDouble(monthly), Double.parseDouble(formatter.format((double) annual / 12.0)));
		assertEquals(Double.parseDouble(weekly), Double.parseDouble(formatter.format((double) annual / 52.0)));
		assertEquals(Double.parseDouble(hourly), Double.parseDouble(formatter.format((double) annual / 52.0 / 40.0)));
	}

	public void setDateOfBirth(String bday) {
		String[] pieces = bday.split(" ");
		String birthDay = pieces[2] + "-" + pieces[1] + "-" + pieces[5];
		driver.findElement(By.xpath("//input[@id='Date-date']")).sendKeys(birthDay);
	}

	public String setGender(int n) {
		if (n == 1) {
			driver.findElement(By.xpath("//input[@value='Male']")).click();
			gender="Male";
		} else {
			driver.findElement(By.xpath("//input[@value='Female']")).click();
			gender="Female";
		}
		return gender;
	}

	@Test
	public void fullNameEmptyTest() {
		// firstly assert that you are on the correct page
		assertEquals(driver.getTitle(), "SDET Job Application");

		driver.findElement(By.xpath("//input[@elname='first']")).clear();
		driver.findElement(By.xpath("//*[@elname='last']")).clear();

		driver.findElement(By.xpath("//em[.=' Next ']")).click();

		String nameError = driver.findElement(By.xpath("//p[@id='error-Name']")).getText();
		assertEquals(nameError, "Enter a value for this field.");
	}
}
