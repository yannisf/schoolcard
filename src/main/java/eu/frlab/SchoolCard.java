package eu.frlab;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SchoolCard {

    private static final long MAX_WAIT_TIME = 15000;

    private static final Logger LOG = LoggerFactory.getLogger(SchoolCard.class);

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public static void main(String[] args) throws Exception {
        String home = System.getenv("HOME");
        Path gsisPath = Paths.get(home + "/.gsis");
        gsisCheck(gsisPath);
        String[] gsis = Files.readAllLines(gsisPath).get(0).split(":");
        argCheck(args);
        String[] school = Files.readAllLines(Paths.get(args[0])).get(0).split(":");
        String[] kid = Files.readAllLines(Paths.get(args[0])).get(1).split(":");
        infoCheck(kid);
        LOG.info("Preparing COVID school card for [{}]", kid[0]);
        SchoolCard schoolCard = new SchoolCard();
        schoolCard.execute(gsis, school, kid);
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
            LOG.error("Kid's information must be in the format 'NAME:SURNAME:FATHER_NAME:MOTHER_NAME:DAY_OF_BIRTH:MONTH_OF_BIRTH:YEAR_OF_BIRTH:AMKA'.");
            System.exit(1);
        }
    }

    private void execute(String[] gsis, String[] school, String[] kid) throws InterruptedException {

        WebDriver driver = getDriver();
        FluentWait<WebDriver> wait = getWait(driver);

        LOG.info("Open edupass.gov.gr");
        String url = "https://edupass.gov.gr/";
        driver.manage().window().maximize();
        driver.get(url);

        LOG.info("Let's start");
        driver.findElement(By.xpath("//span[text()[contains(.,'Ξεκινήστε εδώ')]]")).click();

        LOG.info("Select school level");
        Thread.sleep(500L);
        driver.findElement(By.xpath("//input[@value='schools']")).click();
        Thread.sleep(500L);
        driver.findElement(By.xpath("//span[text()[contains(.,'Συνέχεια')]]")).click();

        LOG.info("Select option type");
        Thread.sleep(500L);
        driver.findElement(By.xpath("//input[@value='studentCard']")).click();
        Thread.sleep(500L);
        driver.findElement(By.xpath("//span[text()[contains(.,'Συνέχεια')]]")).click();

        LOG.info("Accept cookies");
        By acceptCookiesControl = By.xpath("//button[span[text()[contains(.,'Αποδοχή')]]]");
        wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesControl));
        driver.findElement(acceptCookiesControl).click();

        LOG.info("Login on service");
        driver.findElement(By.cssSelector("a[href='/api/login/']")).click();

        LOG.info("Select login authenticator (GSIS)");
        driver.findElement(By.xpath("//button[span[text()[contains(.,'ΓΓΠΣΔΔ')]]]")).click();

        LOG.info("Enter credentials");
        driver.findElement(By.name("j_username")).sendKeys(gsis[0]);
        driver.findElement(By.name("j_password")).sendKeys(gsis[1]);
        driver.findElement(By.id("btn-login-submit")).click();

        LOG.info("Review GSIS data");
        try {
            driver.findElement(By.id("btn-submit")).click();
        } catch (Exception ignored) {
            LOG.info("Review GSIS data (skipped)");
        }

        LOG.info("Review personal information");
        By reviewPersonalInfControl = By.cssSelector("button[label='Συνέχεια']");
        wait.until(ExpectedConditions.elementToBeClickable(reviewPersonalInfControl));
        driver.findElement(reviewPersonalInfControl).click();

        LOG.info("Choose school region");
        By schoolRegion = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(2) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolRegion));
        driver.findElement(schoolRegion).click();

        Thread.sleep(500L);
        By schoolRegionOption = By.xpath("//li[text()[contains(.,'" + school[0] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolRegionOption));
        driver.findElement(schoolRegionOption).click();

        LOG.info("Choose school region division");
        By schoolDivision = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(3) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolDivision));
        driver.findElement(schoolDivision).click();

        Thread.sleep(500L);
        By schoolDivisionOption = By.xpath("//li[text()[contains(.,'" + school[1] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolDivisionOption));
        driver.findElement(schoolDivisionOption).click();

        LOG.info("Choose school municipality");
        By schoolMunicipality = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(4) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolMunicipality));
        driver.findElement(schoolMunicipality).click();

        Thread.sleep(500L);
        By schoolMunicipalityOption = By.xpath("//li[text()[contains(.,'" + school[2] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolMunicipalityOption));
        driver.findElement(schoolMunicipalityOption).click();

        LOG.info("Choose school category");
        By schoolCategory = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(5) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolCategory));
        driver.findElement(schoolCategory).click();

        Thread.sleep(500L);
        By schoolCategoryOption = By.xpath("//li[text()[contains(.,'" + school[3] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolCategoryOption));
        driver.findElement(schoolCategoryOption).click();

        LOG.info("Choose school type");
        By schoolType = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(6) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolType));
        driver.findElement(schoolType).click();

        Thread.sleep(500L);
        By schoolTypeOption = By.xpath("//li[text()[contains(.,'" + school[4] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolTypeOption));
        driver.findElement(schoolTypeOption).click();

        LOG.info("Choose school name");
        By schoolName = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(7) > div > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(schoolName));
        driver.findElement(schoolName).click();

        Thread.sleep(500L);
        By schoolNameOption = By.xpath("//li[text()[contains(.,'" + school[5] + "')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(schoolNameOption));
        driver.findElement(schoolNameOption).click();

        LOG.info("Enter kids personal information");
        driver.findElement(By.name("input_firstname")).sendKeys(kid[0]);
        driver.findElement(By.name("input_lastname")).sendKeys(kid[1]);
        driver.findElement(By.name("input_dob-day")).sendKeys(kid[4]);
        driver.findElement(By.name("input_dob-month")).sendKeys(kid[5]);
        driver.findElement(By.name("input_dob-year")).sendKeys(kid[6]);
        driver.findElement(By.name("input_amka")).sendKeys(kid[7]);

        LOG.info("Enter kids COVID test information");
        LocalDate date = LocalDate.now();
        driver.findElement(By.name("self_test_date-day")).sendKeys(String.valueOf(date.getDayOfMonth()));
        driver.findElement(By.name("self_test_date-month")).sendKeys(String.valueOf(date.getMonthValue()));
        driver.findElement(By.name("self_test_date-year")).sendKeys(String.valueOf(date.getYear()));

        LOG.info("Enter COVID test result");
        By covidResult = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(8) > div:nth-child(2) > div > div > div");
        wait.until(ExpectedConditions.elementToBeClickable(covidResult));
        driver.findElement(covidResult).click();

        Thread.sleep(500L);
        By covidResultOption = By.xpath("//li[text()[contains(.,'ΑΡΝΗΤΙΚΟ')]]");
        wait.until(ExpectedConditions.presenceOfElementLocated(covidResultOption));
        driver.findElement(covidResultOption).click();

        LOG.info("Submit form");
        driver.findElement(By.xpath("//div[text()[contains(.,'Υποβολή')]]")).click();

//        LOG.info("Request print");
//        By printButton = By.cssSelector("button[label='Εκτύπωση']");
//        wait.until(ExpectedConditions.elementToBeClickable(printButton));
//        driver.findElement(printButton).click();
    }

    private WebDriver getDriver() {
        boolean useGecko = false;
        if (useGecko) {
            return new FirefoxDriver();
        } else {
            if (System.getenv("CHROME_DRIVER") != null)
                System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));

            System.setProperty("webdriver.chrome.silentOutput", "true");

            return new ChromeDriver();
        }
    }

    private FluentWait<WebDriver> getWait(WebDriver driver) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver);
        wait.withTimeout(Duration.of(MAX_WAIT_TIME, ChronoUnit.MILLIS));
        wait.pollingEvery(Duration.of(100, ChronoUnit.MILLIS));
        wait.ignoring(NoSuchElementException.class);
        return wait;
    }

}
