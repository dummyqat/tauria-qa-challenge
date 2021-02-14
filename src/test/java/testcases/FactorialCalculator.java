package testcases;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FactorialCalculator {

	static WebDriver driver;
	static String number = "7";
	static String result = "5040";
	static String factorialUrl = "http://qainterview.pythonanywhere.com/factorial";

	public static void main(String[] args) throws Exception {
		browserSetup();
		goToUrl(factorialUrl);
		calculateFactorial(number, result);
		terminateSession();
	}

	public static void browserSetup() {
		WebDriverManager.chromedriver().setup();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	public static void goToUrl(String url) {
		driver.get(url);
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains(url));
	}

	public static void calculateFactorial(String number, String result) throws Exception {
		driver.findElement(By.cssSelector("input[id='number']")).sendKeys(number);
		driver.findElement(By.cssSelector("button[id='getFactorial']")).click();
		waitForElementVisible(By.xpath("//p[contains(text(),'factorial')]"));
		String getResultMsg = driver.findElement(By.cssSelector("p[id='resultDiv']")).getText();
		System.out.println(getResultMsg);
		if (getResultMsg.contentEquals("The factorial of " + number + " is: " + result)) {
			System.out.println("PASSED: The factorial of 7 is calculated correctly.");
		} else {
			System.out.println("FAILED: The factorial of 7 is not calculated correctly.");
		}
	}

	public static void waitForElementVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
	}

	public static void terminateSession() {
		driver.quit();
	}

}
