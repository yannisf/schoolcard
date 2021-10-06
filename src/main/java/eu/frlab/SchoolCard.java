package eu.frlab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class SchoolCard {

    private static final long WAIT_TIME = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(SchoolCard.class);

    public static void main(String[] args) throws Exception {
        String home = System.getenv("HOME");
        Path gsisPath = Paths.get(home + "/.gsis");
        gsisCheck(gsisPath);
        String[] gsis = Files.readAllLines(gsisPath).get(0).split(":");
        argCheck(args);
        String[] kid = Files.readAllLines(Paths.get(args[0])).get(0).split(":");
        infoCheck(kid);
        LOG.info("Preparing COVID school card for [{}]", kid[0]);
        SchoolCard schoolCard = new SchoolCard();
        schoolCard.execute(gsis, kid);
        System.exit(0);
    }

    private WebDriver getDriver() {
        if (System.getenv("CHROME_DRIVER") != null)
            System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));

        System.setProperty("webdriver.chrome.silentOutput", "true");

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        return new ChromeDriver();
    }

    private static void gsisCheck(Path gsisPath) {
        if (!(Files.exists(gsisPath) && Files.isReadable(gsisPath))) {
            LOG.error("A file, named '.gsis', with GSIS (TaxisNet) credentials in the format 'username:password' does not exist in your home directory.");
            System.exit(1);
        }
    }

    private static void argCheck(String[] args) {
        if (args.length != 1) {
            LOG.error("Exactly one parameter is expected, with the path of the file containing the kid's information in the format 'NAME:SURNAME:FATHER_NAME:MOTHER_NAME:DAY_OF_BIRTH:MONTH_OF_BIRTH:YEAR_OF_BIRTH:AMKA'.");
            System.exit(1);
        }
    }

    private static void infoCheck(String[] kid) {
        if (kid.length != 8) {
            System.out.println("Kid's information must be in the format 'NAME:SURNAME:FATHER_NAME:MOTHER_NAME:DAY_OF_BIRTH:MONTH_OF_BIRTH:YEAR_OF_BIRTH:AMKA'.");
            System.exit(1);
        }
    }

    private void execute(String[] gsis, String[] kid) throws Exception {

        WebDriver driver = getDriver();
        LOG.info("Open gov.gr COVID19 School Card web page");
        String url = "https://covid19-self-test.services.gov.gr/templates/COVID19-SCHOOL-CARD2/create/";
        driver.get(url);
        Thread.sleep(WAIT_TIME);

        LOG.info("Accept cookies");
        driver.findElement(By.xpath("//button[span[text()[contains(.,'Αποδοχή')]]]")).click();
        Thread.sleep(WAIT_TIME);

        LOG.info("Login on service");
        driver.findElement(By.cssSelector("a[href='/api/login/']")).click();
        Thread.sleep(WAIT_TIME);

        LOG.info("Select login authenticator (GSIS)");
        driver.findElement(By.xpath("//button[span[text()[contains(.,'ΓΓΠΣΔΔ')]]]")).click();
        Thread.sleep(WAIT_TIME);

        LOG.info("Enter credentials");
        driver.findElement(By.name("j_username")).sendKeys(gsis[0]);
        driver.findElement(By.name("j_password")).sendKeys(gsis[1]);
        driver.findElement(By.id("btn-login-submit")).click();
        Thread.sleep(WAIT_TIME);

        LOG.info("Review GSIS data");
        try {
            driver.findElement(By.id("btn-submit")).click();
            Thread.sleep(WAIT_TIME);
        } catch (Exception ignored) {
            LOG.info("Review GSIS data (skipped)");
        }

        LOG.info("Review personal information");
        driver.findElement(By.cssSelector("button[label='Συνέχεια']")).click();
        Thread.sleep(WAIT_TIME);

        LOG.info("Enter kids personal information");
        driver.findElement(By.name("child_firstname")).sendKeys(kid[0]);
        driver.findElement(By.name("child_surname")).sendKeys(kid[1]);
        driver.findElement(By.name("child_fathername")).sendKeys(kid[2]);
        driver.findElement(By.name("child_mothername")).sendKeys(kid[3]);
        driver.findElement(By.name("child_birth_date-day")).sendKeys(kid[4]);
        driver.findElement(By.name("child_birth_date-month")).sendKeys(kid[5]);
        driver.findElement(By.name("child_birth_date-year")).sendKeys(kid[6]);
        driver.findElement(By.cssSelector("form")).submit();
        Thread.sleep(WAIT_TIME);

        LOG.info("Enter kids COVID test information");
        LocalDate date = LocalDate.now();
        driver.findElement(By.name("amka")).sendKeys(kid[7]);
        driver.findElement(By.name("test_date-day")).sendKeys(String.valueOf(date.getDayOfMonth()));
        driver.findElement(By.name("test_date-month")).sendKeys(String.valueOf(date.getMonthValue()));
        driver.findElement(By.name("test_date-year")).sendKeys(String.valueOf(date.getYear()));
        driver.findElement(By.cssSelector("#mui-component-select-test_result")).click();
        driver.findElement(By.cssSelector("#menu-test_result > div.MuiPaper-root.MuiMenu-paper.MuiPopover-paper.MuiPaper-elevation8.MuiPaper-rounded > ul > li:nth-child(1)")).click();

        LOG.info("Submit form");
        Thread.sleep(WAIT_TIME);
        driver.findElement(By.cssSelector("form")).submit();

        LOG.info("Request print");
        Thread.sleep(WAIT_TIME);
        driver.findElement(By.cssSelector("button[label='Εκτύπωση']")).click();
    }

}
