package com.mockaroo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.*;
import static org.testng.Assert.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
	
	WebDriver driver;
	
	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		driver.get("https://mockaroo.com/");
	}
	
//	@BeforeMethod
//	public void navigatToHomePage() {
//		
//	}
	
	@Test (priority=1)
	public void verifyTitle() {
		String actual = driver.getTitle();
	    String expected = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
	    assertEquals(actual, expected);
	}
	
	@Test (priority=2)
	public void verifyBrand() {
		String actual = driver.findElement(By.xpath("//div[@class='brand']")).getText();
		String expected = "mockaroo";
		String actual2 = driver.findElement(By.xpath("//div[@class='tagline']")).getText();
		String expected2 = "realistic data generator";
		
		assertEquals(actual, expected);
		assertEquals(actual2, expected2);
	}
	
	@Test (priority=3)
	public void removeFields() {
		for (int i = 1; i < 7; i++) {
			driver.findElement(By.xpath("//div[@class='fields'][" + i + "]/div[@class='column column-remove']/a[@class='close remove-field remove_nested_fields']")).click();
		}
	}

	@Test (priority=4)
	public void verifyFieldTypeOptions() {
		String actual = driver.findElement(By.xpath("//div[@class='column column-header column-name']")).getText();
		String expected = "Field Name";
		String actual2 = driver.findElement(By.xpath("//div[@class='column column-header column-type']")).getText();
		String expected2 = "Type";
		String actual3 = driver.findElement(By.xpath("//div[@class='column column-header column-options']")).getText();
		String expected3 = "Options";
		
		assertEquals(actual, expected);
		assertEquals(actual2, expected2);
		assertEquals(actual3, expected3);
	}
	
	@Test (priority=5)
	public void addAnotherField() {
		assertTrue(driver.findElement(By.xpath("//a[.='Add another field']")).isEnabled());
	}
	
	@Test (priority=6) //8
	public void defaultNumber() {
		assertEquals(driver.findElement(By.xpath("//input[@id='num_rows']")).getAttribute("value"), "1000");
	}
	
	@Test (priority=7) //9
	public void defaultFormat() {
		assertEquals(driver.findElement(By.xpath("//select[@id='schema_file_format']/option[@value='csv']")).getText(), "CSV");
	}
	
	@Test (priority=8) //10
	public void lineEnding() {
		assertEquals(driver.findElement(By.xpath("//select[@id='schema_line_ending']/option[@value='unix']")).getText(), "Unix (LF)");
	}
	
	@Test (priority=9) //11
	public void checkbox() {
		assertTrue(driver.findElement(By.xpath("//input[@id='schema_include_header']")).isSelected());
		assertFalse(driver.findElement(By.xpath("//input[@id='schema_bom']")).isSelected());
	}
	
	@Test (priority=10) //12
	public void clickAnotherField() {
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		driver.findElement(By.xpath("//div[@class='fields'][7]/div[@class='column']/input[@id[starts-with(., 'schema_columns_attributes')]]")).sendKeys("City");
	}
	
	@Test (priority=11) //13
	public void chooseType() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='fields'][7]//input[@class='btn btn-default']")).click();
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.xpath("//div[@id='type_dialog']")).isDisplayed());
	}
	
	@Test (priority=12) //14
	public void searchCity() {
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("city");
		driver.findElement(By.xpath("//div[@class='type']")).click();
	}
	
	@Test (priority=13) //15
	public void country() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		driver.findElement(By.xpath("//div[@class='fields'][8]/div[@class='column']/input[@id[starts-with(., 'schema_columns_attributes')]]")).sendKeys("Country");
		driver.findElement(By.xpath("//div[@class='fields'][8]//input[@class='btn btn-default']")).click();
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.xpath("//div[@id='type_dialog']")).isDisplayed());
		driver.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("country");
		driver.findElement(By.xpath("//div[.='Country']")).click();
	}

	@Test (priority=14) //16
	public void downloadData() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[.='Download Data']")).click();
	}
	
	@Test (priority=15)//17
	public void bReader() throws InterruptedException {
		Thread.sleep(3000);
		try (	FileReader fr = new FileReader("/Users/app/Downloads/MOCK_DATA.csv");
				BufferedReader br = new BufferedReader(fr);) {
			assertEquals(br.readLine(), "City,Country"); //18
			int count = 0;
			while(br.readLine() != null) {
				count++;
			}
			assertEquals(String.valueOf(count), "1000"); //19
		}
		catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	@Test (priority=16) //20
	public void c() {
		List<String> cities = new ArrayList<>();
		List<String> countries = new ArrayList<>();
		try (	FileReader fr = new FileReader("/Users/app/Downloads/MOCK_DATA.csv");
				BufferedReader br = new BufferedReader(fr);) {
			String v = "";
			while((v = br.readLine()) != null) {
				cities.add(v.substring(0, v.indexOf(","))); //20
				countries.add(v.substring(v.indexOf(",")+1)); //21
			}
			
		}
		catch (IOException e) {
		e.printStackTrace();
		}
		cities.remove(0);
		countries.remove(0);
		Collections.sort(cities); //22
		Collections.sort(countries);
//		System.out.println(cities);
//		System.out.println(countries);
		
		int longest = cities.get(0).length();
		String longestCity = cities.get(0);
		int shortest = cities.get(0).length();
		String shortestCity = cities.get(0);
		for (int i = 1; i < cities.size(); i++) {
			if (cities.get(i).length() > longest) {
				longest = cities.get(i).length();
				longestCity = cities.get(i);
			}
			if (cities.get(i).length() < shortest) {
				shortest = cities.get(i).length();
				shortestCity = cities.get(i);
			}
		}
		System.out.println(longestCity); //22
		System.out.println(shortestCity); //22
		
		Set<String> uniqueCountries = new HashSet<>(countries);
		for (String i : uniqueCountries) {
			int count = 0;
			for (int j = 0; j < countries.size(); j++) {
				if (i.equals(countries.get(j))) {
					count++;
				}
			}
			System.out.println(i + "-" + count); //23
		}
		
		Set<String> citiesSet = new HashSet<>(cities); //24
		int countCities = 1;
		for (int i = 0; i < cities.size()-1; i++) {
			if (!cities.get(i+1).equals(cities.get(i)))
					countCities++;
		}
		assertEquals(String.valueOf(countCities), String.valueOf(citiesSet.size())); //25
		
		Set<String> countrySet = new HashSet<>(countries); //26
		int countCountries = 1;
		for (int i = 0; i < countries.size()-1; i++) {
			if (!countries.get(i+1).equals(countries.get(i)))
				countCountries++;
		}
		System.out.println(countrySet.size());
		System.out.println(countCountries);
		assertEquals(String.valueOf(countCountries), String.valueOf(countrySet.size())); //27
		
	}

}
