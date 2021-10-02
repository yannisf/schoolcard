package eu.frlab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class SchoolCard {

    public static void main(String[] args) throws Exception {
        String home = System.getenv("HOME");
        String[] gsis = Files.readAllLines(Paths.get(home + "/.gsis")).get(0).split(":");
        String[] kid = Files.readAllLines(Paths.get(args[0])).get(0).split(":");
        SchoolCard schoolCard = new SchoolCard();
        schoolCard.execute(gsis, kid);
    }

    private void execute(String[] gsis, String[] kid) throws Exception {
        String url = "https://covid19-self-test.services.gov.gr/templates/COVID19-SCHOOL-CARD2/create/";

        //System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        Thread.sleep(500);
        driver.findElement(By.cssSelector("a[href='/api/login/']")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("#__next > div > div.MuiContainer-root.MuiBasicLayoutContent-root.MuiContainer-maxWidthLg > div > div > div.MuiGrid-root.MuiGrid-container.MuiGrid-spacing-xs-3 > div:nth-child(1) > div > div > button")).click();
        Thread.sleep(500);
        driver.findElement(By.name("j_username")).sendKeys(gsis[0]);
        driver.findElement(By.name("j_password")).sendKeys(gsis[1]);
        driver.findElement(By.id("btn-login-submit")).click();
        Thread.sleep(500);
        try {
            driver.findElement(By.id("btn-submit")).click();
        } catch (Exception ignored) {
        }
        Thread.sleep(500);
        driver.findElement(By.cssSelector("button[label='Συνέχεια']")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("#content > div:nth-child(1) > div > div > div.MuiSnackbarContent-action > button.MuiButtonBase-root.MuiButton-root.jss259.MuiButton-contained.jss245.MuiButton-containedPrimary.MuiButton-containedSizeSmall.MuiButton-sizeSmall")).click();
        driver.findElement(By.name("child_firstname")).sendKeys(kid[0]);
        driver.findElement(By.name("child_surname")).sendKeys(kid[1]);
        driver.findElement(By.name("child_fathername")).sendKeys(kid[2]);
        driver.findElement(By.name("child_mothername")).sendKeys(kid[3]);
        driver.findElement(By.name("child_birth_date-day")).sendKeys(kid[4]);
        driver.findElement(By.name("child_birth_date-month")).sendKeys(kid[5]);
        driver.findElement(By.name("child_birth_date-year")).sendKeys(kid[6]);
        driver.findElement(By.cssSelector("form")).submit();
        Thread.sleep(500);
        LocalDate date = LocalDate.now();
        driver.findElement(By.name("amka")).sendKeys(kid[7]);
        driver.findElement(By.name("test_date-day")).sendKeys(String.valueOf(date.getDayOfMonth()));
        driver.findElement(By.name("test_date-month")).sendKeys(String.valueOf(date.getMonthValue()));
        driver.findElement(By.name("test_date-year")).sendKeys(String.valueOf(date.getYear()));
        driver.findElement(By.cssSelector("#mui-component-select-test_result")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("#menu-test_result > div.MuiPaper-root.MuiMenu-paper.MuiPopover-paper.MuiPaper-elevation8.MuiPaper-rounded > ul > li:nth-child(1)")).click();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("form")).submit();
        Thread.sleep(500);
        driver.findElement(By.cssSelector("button[label='Εκτύπωση']")).click();
    }

}
