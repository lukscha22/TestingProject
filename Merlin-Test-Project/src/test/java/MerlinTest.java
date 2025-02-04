import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.Set;

public class MerlinTest {

    public WebDriver driver;
    public WebDriverWait wait;
    public String testURL = "https://www.google.com";

    @BeforeClass
    public void setupTest() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to(testURL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(priority = 1)
    public void GoogleSearchTest() {
        WebElement rejectAllButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"W0wltc\"]/div")));
        rejectAllButton.click();

        WebElement searchTextBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("APjFqb")));
        searchTextBox.sendKeys("Merlin");
        searchTextBox.submit();

        WebElement testLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Portal sustava Merlin')]")));
        Assert.assertEquals(testLink.getText(), "Portal sustava Merlin", "Search result text does not match!");
        System.out.println("Search result text: " + testLink.getText());
    }

    @Test(priority = 2)
    public void ClickMerlinPageTest() {
        WebElement testLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[contains(text(), 'Portal sustava Merlin')]")));
        testLink.click();

        wait.until(ExpectedConditions.titleContains("Merlin"));
        Assert.assertTrue(driver.getTitle().contains("Merlin"), "Page title is incorrect!");
        System.out.println("Merlin page successfully reached.");
    }

    @Test(priority = 3)
    public void ClickInfoButtonTest() {
        WebElement testButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[contains(text(), 'o Merlinu')]]")));
        testButton.click();

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("aboutModalLabel")));
        Assert.assertTrue(modal.isDisplayed(), "Modal did not appear!");

        WebElement closeModal = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-close")));
        closeModal.click();
        System.out.println("Info button working properly.");
    }

    @Test(priority = 4)
    public void OpenLoginPageTest() {
        WebElement testButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[6]/div/div[1]/div/a")));
        testButton.click();

        Set<String> allWindows = driver.getWindowHandles();
        String originalWindow = driver.getWindowHandle();
        String newWindow = "";

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                newWindow = windowHandle;
                break;
            }
        }

        driver.switchTo().window(newWindow);

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Prijavi se na Merlin 2024/2025')]")));
        Assert.assertEquals(title.getText(), "Prijavi se na Merlin 2024/2025", "Opened page is not login page!");
        System.out.println("Login page opened successfully.");
    }

    @Test(priority = 5, dependsOnMethods = "OpenLoginPageTest")
    public void GuestLoginTest() {
        WebElement guestButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginguestbtn")));
        guestButton.click();

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Moja naslovnica (Gost)')]")));
        Assert.assertTrue(title.getText().contains("Moja naslovnica (Gost)"), "Unable to reach guest login page!");
        System.out.println("Successfully logged in as guest.");
    }

    @AfterClass
    public void teardownTest() {
        driver.quit();
    }
}