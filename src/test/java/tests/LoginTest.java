package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;

import java.util.Random;

public class LoginTest extends BaseTest {

    private static final String emptyFieldsError = "Please fill out Username and Password.";
    private static final String wrongUserError = "User does not exist.";
    private static final String wrongPasswordError = "Wrong password.";
    private static final String positiveSignupMessage = "Sign up successful.";
    private static final String existingUserError = "This user already exist.";

    private static final String existingUsername = "creativeuser";
    private static final String existingPassword = "newpassword";

    private String generateRandomString(int length) {
        if (length <= 0 || length > 20) {
            length = 8;
        }

        String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuv";

        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericCharacters.length());
            char randomChar = alphanumericCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    @Test
    public void checkNegativeLoginWithEmptyUsername() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.login("", "somewrongpassword"),
                emptyFieldsError
        );
    }

    @Test
    public void checkNegativeLoginWithEmptyPassword() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.login("somewronguser", ""),
                emptyFieldsError
        );
    }

    @Test
    public void checkNegativeLoginWithEmptyFields() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.login("", ""),
                emptyFieldsError
        );
    }

    @Test
    public void checkNegativeLoginWithWrongCredentials() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.login(generateRandomString(8), "somewrongpassword"),
                wrongUserError
        );
    }

    @Test
    public void checkNegativeSignupWithEmptyUsername() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.signup("", "somewrongpassword"),
                emptyFieldsError
        );
    }

    @Test
    public void checkNegativeSignupWithEmptyPassword() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.signup("somebadusername", ""),
                emptyFieldsError
        );
    }

    @Test
    public void checkNegativeSignupWithEmptyCredentials() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.signup("", ""),
                emptyFieldsError
        );
    }

    /* a bad test written out of interest */
    /*
    @Test
    public void checkPositiveSignup() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.signup(generateRandomString(8), "password"),
                positiveSignupMessage
        );
    }
    */

    @Test
    public void checkNegativeSignupWithExistingUsername() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.signup(existingUsername, existingPassword),
                existingUserError
        );

        /* this test fails */
        Assert.assertEquals(
                page.signup(existingUsername, "wrongpassword"),
                existingUserError
        );
    }

    @Test
    public void checkPositiveLogin() {
        LoginPage page = new LoginPage(driver);
        /*
        String username = generateRandomString(8);
        String password = "password";
        page.signup(username, password);
        */

        page.login(existingUsername, existingPassword);

        Assert.assertEquals(
                page.getWelcomeMessage(),
                String.format("Welcome %s", existingUsername)
        );
    }

    @Test
    public void checkNegativeLoginWithWrongPassword() {
        LoginPage page = new LoginPage(driver);

        Assert.assertEquals(
                page.login(existingUsername, "wrongpassword"),
                wrongPasswordError
        );
    }
}
