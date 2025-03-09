package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(LoginPage.class);

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private static final String LOGIN_FORM_TITLE = "Log in";
    private static final String SIGNUP_FORM_TITLE = "Sign up";

    private static final By LOGIN_FORM = By.id("login2");
    private static final By SIGNUP_FORM = By.id("signin2");

    /* searches for the root of the target form '%s' ("Log in" or "Sign up"), and then the input fields (the first match is the username field, the second is the password field). */
    private static final String USERNAME_FIELD_PATTERN = "//h5[text() = '%s']/ancestor::div//div[@class = 'form-group'][1]//input";
    private static final String PASSWORD_FIELD_PATTERN = "//h5[text() = '%s']/ancestor::div//div[@class = 'form-group'][2]//input";

    /* searches for the root of the target form '%s' ("Log in" or "Sign up"), and then a button in the footer (the first match is the 'Close' button, the second is the submit button). */
    private static final String SUBMIT_BUTTON_PATTERN = "//h5[text() = '%s']/ancestor::div//div[@class = 'modal-footer']//button[2]";
    private static final String CLOSE_BUTTON_PATTERN  = "//h5[text() = '%s']/ancestor::div//div[@class = 'modal-footer']//button[1]";

    private void openForm(String formTitle) {
        log.info("Opening the main page");
        driver.get("https://www.demoblaze.com/#");

        log.info("Opening the '{}' form", formTitle);
        driver.findElement(formTitle.equals(LOGIN_FORM_TITLE) ? LOGIN_FORM : SIGNUP_FORM).click();
    }

    private void closeForm(String formTitle) {
        log.info("Closing the '{}' form", formTitle);
        try {
            driver.findElement(By.xpath(String.format(CLOSE_BUTTON_PATTERN, formTitle))).click();
        } catch (Exception e) {
            log.info("Failed to close the '{}' form", formTitle);
            return;
        }
    }

    private String sendForm(String formTitle, String username, String password) {
        openForm(formTitle);

        log.info("Sending the '{}' form with username '{}' and password '{}'", formTitle, username, password);
        driver.findElement(By.xpath(String.format(USERNAME_FIELD_PATTERN, formTitle))).sendKeys(username);
        driver.findElement(By.xpath(String.format(PASSWORD_FIELD_PATTERN, formTitle))).sendKeys(password);
        driver.findElement(By.xpath(String.format(SUBMIT_BUTTON_PATTERN, formTitle))).click();

        String message = getAlertMessage();
        if (message != null && !message.isEmpty()) {
            closeForm(formTitle);
        }

        return message;
    }

    public String login(String username, String password) {
        return sendForm(LOGIN_FORM_TITLE, username, password);
    }

    public String signup(String username, String password) {
        return sendForm(SIGNUP_FORM_TITLE, username, password);
    }

    public String getWelcomeMessage() {
        log.info("Getting welcome message");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            By elementBy = By.id("nameofuser");
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementBy));

            return driver.findElement(elementBy).getText();
        } catch (Exception e) {
            log.info("Failed to get welcome message");
            return null;
        }
    }

    private String getAlertMessage() {
        log.info("Getting alert message");
        try {
            /* timer for checking the appearance of the prompt window */
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            String message = alert.getText();
            alert.accept();
            return message;
        } catch (Exception e) {
            log.info("Failed to get alert message");
            return null;
        }
    }
}
