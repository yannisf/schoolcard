package eu.frlab;

import eu.frlab.helper.CliHelper;
import eu.frlab.helper.DataParser;
import eu.frlab.helper.ParsedOptions;
import eu.frlab.model.CredentialsDataModel;
import eu.frlab.model.KidDataModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SchoolCard {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(SchoolCard.class);

    public static void main(String[] args) throws Exception {
        CliHelper cliHelper = new CliHelper(args);
        ParsedOptions parsedOptions = cliHelper.getParsedOptions();
        cliHelper.handleHelp();

        CredentialsDataModel credentials = DataParser.parseCredentialsData(parsedOptions.getCredentials());
        KidDataModel kid = DataParser.parseKidData(parsedOptions.getKid());

        LOG.info("Προετοιμασία έκδοσης σχολικής κάρτας COVID για [{}]", kid.getKidFullName());

        SchoolCard schoolCard = new SchoolCard();
        schoolCard.execute(parsedOptions, credentials, kid);
    }

    private void execute(ParsedOptions parsedOptions, CredentialsDataModel credentials, KidDataModel kid) throws InterruptedException {

        WebDriver driver = getDriver(parsedOptions);
        driver.manage().timeouts().implicitlyWait(Duration.of(5, ChronoUnit.SECONDS));
        long customWait = parsedOptions.getWait();

        LOG.info("Άνοιγμα σελίδας edupass.gov.gr");
        String url = "https://edupass.gov.gr/";
        driver.manage().window().maximize();
        driver.get(url);

        LOG.info("Ας ξεκινήσουμε");
        driver.findElement(By.xpath("//span[text()[contains(.,'Ξεκινήστε εδώ')]]")).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή εκπαιδευτικής βαθμίδας");
        driver.findElement(By.xpath("//input[@value='schools']")).click();
        Thread.sleep(customWait);

        driver.findElement(By.xpath("//span[text()[contains(.,'Συνέχεια')]]")).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή σχολικής κάρτας");
        driver.findElement(By.xpath("//input[@value='studentCard']")).click();
        Thread.sleep(customWait);

        driver.findElement(By.xpath("//span[text()[contains(.,'Συνέχεια')]]")).click();
        Thread.sleep(customWait);

        LOG.info("Αποδοχή cookies");
        By acceptCookiesControl = By.xpath("//button[span[text()[contains(.,'Αποδοχή')]]]");
        driver.findElement(acceptCookiesControl).click();
        Thread.sleep(customWait);

        LOG.info("Αυθεντικοποίηση");
        driver.findElement(By.cssSelector("a[href='/api/login/']")).click();

        LOG.info("Επιλογή υπηρεσίας αυθεντικοποίησης (ΓΓΠΣΔΔ)");
        driver.findElement(By.xpath("//button[span[text()[contains(.,'ΓΓΠΣΔΔ')]]]")).click();

        LOG.info("Εισαγωγή διαπιστευτηρίων");
        driver.findElement(By.name("j_username")).sendKeys(credentials.getUsername());
        driver.findElement(By.name("j_password")).sendKeys(credentials.getPassword());
        driver.findElement(By.id("btn-login-submit")).click();

        LOG.info("Ανασκόπηση δεδομένων ΓΓΠΣΔΔ");
        try {
            driver.findElement(By.id("btn-submit")).click();
        } catch (Exception ignored) {
            LOG.info("Ανασκόπηση δεδομένων ΓΓΠΣΔΔ (παραλείφθηκε)");
        }

        LOG.info("Ανασκόπηση προσωπικών δεδομένων");
        By reviewPersonalInfControl = By.cssSelector("button[label='Συνέχεια']");
        driver.findElement(reviewPersonalInfControl).click();
        Thread.sleep(customWait);

        String xpathContainsFormat = "//li[text()[contains(.,'%s')]]";

        LOG.info("Επιλογή Περιφέρειας σχολείου");
        By schoolRegion = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(2) > div > div > div > div");
        driver.findElement(schoolRegion).click();
        Thread.sleep(customWait);

        String schoolRegionXpathExpression = String.format(xpathContainsFormat, kid.getSchoolRegion());
        By schoolRegionOption = By.xpath(schoolRegionXpathExpression);
        driver.findElement(schoolRegionOption).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή Περιφερειακής Ενότητας σχολείου");
        By schoolDivision = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(3) > div > div > div > div");
        driver.findElement(schoolDivision).click();
        Thread.sleep(customWait);

        String schoolDivisionXpathExpression = String.format(xpathContainsFormat, kid.getSchoolDivision());
        By schoolDivisionOption = By.xpath(schoolDivisionXpathExpression);
        driver.findElement(schoolDivisionOption).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή Δήμου σχολείου");
        By schoolMunicipality = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(4) > div > div > div > div");
        driver.findElement(schoolMunicipality).click();
        Thread.sleep(customWait);

        String schoolMunicipalityXpathExpression = String.format(xpathContainsFormat, kid.getSchoolMunicipality());
        By schoolMunicipalityOption = By.xpath(schoolMunicipalityXpathExpression);
        driver.findElement(schoolMunicipalityOption).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή Κατηγορίας σχολείου");
        By schoolCategory = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(5) > div > div > div > div");
        driver.findElement(schoolCategory).click();
        Thread.sleep(customWait);

        String schoolCategoryXpathExpression = String.format(xpathContainsFormat, kid.getSchoolCategory());
        By schoolCategoryOption = By.xpath(schoolCategoryXpathExpression);
        driver.findElement(schoolCategoryOption).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή Τύπου σχολείου");
        By schoolType = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(6) > div > div > div > div");
        driver.findElement(schoolType).click();
        Thread.sleep(customWait);

        String schoolTypeXpathExpression = String.format(xpathContainsFormat, kid.getSchoolType());
        By schoolTypeOption = By.xpath(schoolTypeXpathExpression);
        driver.findElement(schoolTypeOption).click();
        Thread.sleep(customWait);

        LOG.info("Επιλογή Σχολείου");
        By schoolName = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(4) > div > div > div:nth-child(7) > div > div > div > div");
        driver.findElement(schoolName).click();
        Thread.sleep(customWait);

        String schoolNameXpathExpression = String.format(xpathContainsFormat, kid.getSchoolName());
        By schoolNameOption = By.xpath(schoolNameXpathExpression);
        driver.findElement(schoolNameOption).click();
        Thread.sleep(customWait);

        LOG.info("Εισαγωγή προσωπικών δεδομένων μαθητή");
        driver.findElement(By.name("input_firstname")).sendKeys(kid.getKidName());
        driver.findElement(By.name("input_lastname")).sendKeys(kid.getKidSurname());
        driver.findElement(By.name("input_dob-day")).sendKeys(kid.getKidDayOfBirth());
        driver.findElement(By.name("input_dob-month")).sendKeys(kid.getKidMonthOfBirth());
        driver.findElement(By.name("input_dob-year")).sendKeys(kid.getKidYearOfBirth());
        driver.findElement(By.name("input_amka")).sendKeys(kid.getKidAmka());

        LOG.info("Εισαγωγή ημερομηνίας τεστ COVID (σήμερα)");
        LocalDate date = LocalDate.now();
        driver.findElement(By.name("self_test_date-day")).sendKeys(String.valueOf(date.getDayOfMonth()));
        driver.findElement(By.name("self_test_date-month")).sendKeys(String.valueOf(date.getMonthValue()));
        driver.findElement(By.name("self_test_date-year")).sendKeys(String.valueOf(date.getYear()));

        LOG.info("Εισαγωγή αποτελέσματος COVID (ΑΡΝΗΤΙΚΟ)");
        By covidResult = By.cssSelector("#content > div.MuiContainer-root.jss269.MuiContainer-maxWidthLg > div > div > form > div > div > div:nth-child(8) > div:nth-child(2) > div > div > div");
        driver.findElement(covidResult).click();
        Thread.sleep(customWait);

        By covidResultOption = By.xpath("//li[text()[contains(.,'ΑΡΝΗΤΙΚΟ')]]");
        driver.findElement(covidResultOption).click();
        Thread.sleep(customWait);

        if (parsedOptions.isIssue()) {
            LOG.info("Υποβολή");
            driver.findElement(By.xpath("//div[text()[contains(.,'Υποβολή')]]")).click();
            Thread.sleep(customWait);
            By codeSelector = By.cssSelector("#content > div.MuiContainer-root.jss631.MuiContainer-maxWidthLg > div > div > form > div > div > div > div.MuiGrid-root.mainCol.MuiGrid-item.MuiGrid-grid-md-8 > div.MuiGrid-root.step-web-display.MuiGrid-container.MuiGrid-spacing-xs-2 > div.MuiBox-root.jss742.successBox > div > div:nth-child(3) > h2");
            String code = driver.findElement(codeSelector).getText();
            LOG.info("Κωδικός εγγράφου: [{}]", code);
        }

        if (parsedOptions.isPrint()) {
            Thread.sleep(customWait);
            LOG.info("Εκτύπωση");
            By printButton = By.cssSelector("#content > div.MuiContainer-root.jss631.MuiContainer-maxWidthLg > div > div > form > div > div > div > div.MuiGrid-root.jss737.sideCol.MuiGrid-item.MuiGrid-grid-md-4 > div.MuiGrid-root.MuiGrid-container > div:nth-child(2) > div > button");
            driver.findElement(printButton).click();
        }

    }

    private WebDriver getDriver(ParsedOptions options) {
        if (options.isFirefox()) {
            return new FirefoxDriver();
        } else {
            if (System.getenv("CHROME_DRIVER") != null)
                System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));

            System.setProperty("webdriver.chrome.silentOutput", "true");

            return new ChromeDriver();
        }
    }

}
