package testcases;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TauriaRegistration {

	static WebDriver driver;
	private final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static char[] ALPHANUMERIC = (LETTERS + "0123456789").toCharArray();
	static String url = "https://tauria.com/signin";
	static String businessName = "ClarissaQa" + generateRandomString(5);
	static String fname = "Clarissa";
	static String lname = "Macasaquit";
	static String email = "clarissaqa_" + generateRandomString(5) + "@testxyz.com";
	static String password = "Testuser@123";

	public static void main(String[] args) {
		browserSetup();
		goToUrl(url);
		createNewOrganization(businessName, fname, lname, email, password);
		terminateSession();
	}

	public static void browserSetup() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	public static void goToUrl(String url) {
		driver.get(url);
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains(url));
	}

	public static String generateRandomString(int length) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			result.append(ALPHANUMERIC[new Random().nextInt(ALPHANUMERIC.length)]);
		}
		String randomStr = result.toString();
		return randomStr;
	}

	public static void createNewOrganization(String businessName, String fname, String lname, String email,
			String password) {
		driver.findElement(By.xpath("//button[text()='CREATE ONE']")).click();
		waitForElementVisible(By.cssSelector("input[id='name'"));
		driver.findElement(By.cssSelector("input[id='name'")).sendKeys(businessName);
		driver.findElement(By.xpath("//button[text()='NEXT']")).click();
		waitForElementVisible(By.cssSelector("input[id='first']"));
		driver.findElement(By.cssSelector("input[id='first']")).sendKeys(fname);
		driver.findElement(By.cssSelector("input[id='last']")).sendKeys(lname);
		driver.findElement(By.cssSelector("input[id='email']")).sendKeys(email);
		driver.findElement(By.xpath("//button[text()='NEXT']")).click();
		waitForElementVisible(By.cssSelector("input[id='password']"));
		driver.findElement(By.cssSelector("input[id='password']")).sendKeys(password);
		driver.findElement(By.cssSelector("input[id='confirm']")).sendKeys(password);
		driver.findElement(By.xpath("//button[text()='NEXT']")).click();
		waitForElementNotVisible(By.xpath("//img[contains(@src, 'media/loader')]"));
		waitForElementNotVisible(By.cssSelector("div[class='preload-full-container']"));
		waitForElementNotVisible(By.xpath("//img[contains(@src, 'media/loader')]"));
		waitForElementVisible((By.cssSelector("div[class='OnboardingChoice'")));
		WebElement modal = driver.findElement(By.cssSelector("div[class='OnboardingChoice'"));
		if (modal.isDisplayed()) {
			System.out.println("PASSED: Onboarding modal is visible");
		} else {
			System.out.println("FAILED: Onboarding modal is not visible");
		}
		String modalTitle = driver.findElement(By.cssSelector("div[class='OnboardingChoice-title']")).getText();
		if (modalTitle.contentEquals("What would you like to start?")) {
			System.out.println("PASSED: Modal Header contains 'What would you like to start?'");
		} else {
			System.out.println("FAILED: Modal Header does not contains 'What would you like to start?'");
		}
	}

	public static void waitForElementVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
	}

	public static void waitForElementNotVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.pollingEvery(10, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public static void terminateSession() {
		driver.quit();
	}

}
