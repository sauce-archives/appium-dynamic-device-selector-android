import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.net.URL;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicTestSetup {

	private AppiumDriver driver;

	private final static String EXPECTED_RESULT_FOUR = "4";
	private final static String EXPECTED_RESULT_NAN = "NaN";

	@Before
	public void setUp() throws Exception {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY")); // API key through env variable

		capabilities.setCapability("testobject_app_id", System.getenv("TESTOBJECT_APP_ID"));

		capabilities.setCapability("platformName", "android");
		capabilities.setCapability("platformVersion", System.getenv("PLATFORM_VERSION"));

		String appiumVersion = System.getenv("TESTOBJECT_APPIUM_VERSION");
		if (appiumVersion != null && appiumVersion.trim().isEmpty() == false) {
			capabilities.setCapability("testobject_appium_version", appiumVersion);
		}

		String cacheDevice = System.getenv("TESTOBJECT_CACHE_DEVICE");
		if (cacheDevice != null && cacheDevice.trim().isEmpty() == false) {
			capabilities.setCapability("testobject_cache_device", cacheDevice);
		}

		String testUUID = UUID.randomUUID().toString();
		System.out.println("TestUUID: " + testUUID);
		capabilities.setCapability("testobject_testuuid", testUUID);

		driver = new AndroidDriver(new URL(System.getenv("APPIUM_SERVER")), capabilities);

		System.out.println(driver.getCapabilities().getCapability("testobject_test_report_url"));
		System.out.println(driver.getCapabilities().getCapability("testobject_test_live_view_url"));
	}

	/* We disable the driver after EACH test has been executed. */
	@After
	public void tearDown() {
		driver.quit();
	}

	/* A simple addition, it expects the correct result to appear in the result field. */
	@Test
	public void twoPlusTwoOperation() {

        /* Get the elements. */
		MobileElement buttonTwo = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/digit2")));
		MobileElement buttonPlus = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/plus")));
		MobileElement buttonEquals = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/equal")));
		MobileElement resultField = (MobileElement) (driver.findElement(By.xpath("//android.widget.EditText[1]")));

        /* Add two and two. */
		buttonTwo.click();
		buttonPlus.click();
		buttonTwo.click();
		buttonEquals.click();

        /* Check if within given time the correct result appears in the designated field. */
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_FOUR));

	}

	/* A simple zero divided by zero operation. */
	@Test
	public void zerosDivisionOperation() {

        /* Get the elements. */
		MobileElement digitZero = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/digit0")));
		MobileElement buttonDivide = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/div")));
		MobileElement buttonEquals = (MobileElement) (driver.findElement(By.id("net.ludeke.calculator:id/equal")));
		MobileElement resultField = (MobileElement) (driver.findElement(By.xpath("//android.widget.EditText[1]")));

        /* Divide zero by zero. */
		digitZero.click();
		buttonDivide.click();
		digitZero.click();
		buttonEquals.click();

        /* Check if within given time the correct error message appears in the designated field. */
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_NAN));

	}

}
