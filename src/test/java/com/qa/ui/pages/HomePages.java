package com.qa.ui.pages;

import io.restassured.http.ContentType;

import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import java.util.*;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class HomePages extends WebDriverWrapper {
    private static final String TRELLO_URL = "https://api.trello.com/1/";
    private static final String KEY = "7b1305137562dc4330eba7ad4f467c2d";
    private static final String TOKEN = "f16e54225406ad2a9e10572624a399d2eb85e111c324d2a791fc1a58d2757866";
    private static final String NAME = "Hello World!!";
    private final static String HOME_PAGE_URL = "https://trello.com/login";
    private static final By USERNAME = By.xpath("//*[@id='user']");
    private static final By CONTINUE = By.xpath("//*[@id='login']");
    private static final By PASSWORD = By.xpath("//*[@id='password']");
    private static final By LOGIN = By.xpath("//*[@id='login-submit']");
    private static final By BOARDS =
            By.xpath("//*[@aria-label='Create board or Workspace']");

    private static final By CREATE_BOARDS = By.xpath("//*[text()='Create board']");
    private static final By BOARD_TITLE = By.xpath("//*[@data-test-id='create-board-title-input']");
    private static final By CREATE = By.xpath("//*[@data-test-id='create-board-submit-button']");
    private static final By BOARD_TITLE_NAME =
            By.xpath("//*[@title='Patil' and @class='board-tile-details-name']");

    private static String cardId;

    public HomePages(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Navigate to the trello login page
     */
    public void navigateHomePage() {
        driver.navigate().to(HOME_PAGE_URL);
    }

    /**
     * Login with valid username and password
     * @param username user's username
     * @param password user's password
     */
    public void login(String username, String password) {
        click(USERNAME);
        fill(USERNAME, username);
        click(CONTINUE);
        click(PASSWORD);
        fill(PASSWORD, password);
        click(LOGIN);
    }

    /**
     * User create a board
     * @param boardTitle title of the board
     */
    public void createBoard(String boardTitle) {
        click(BOARDS);
        click(CREATE_BOARDS);
        click(BOARD_TITLE);
        fill(BOARD_TITLE, boardTitle);
        click(CREATE);
    }

    /**
     * User validates the board name
     */
    public boolean validateBoardTitle() {
        String boardTitleValidation = findElementByVisibility
                (By.xpath(
                        "//*[contains(@class,'board-header-btn') and contains(@class,'mod-board-name')]"))
                .getText();
        if (boardTitleValidation.equalsIgnoreCase("Solutions")) {
            return true;
        }
        return false;
    }

    /**
     * User gets the card Ids
     */
    public void getCardIds() {
        HashMap data = new HashMap();
        data.put("fields", "name");
        data.put("key", KEY);
        data.put("token", TOKEN);

        Response res = given()
                .contentType(ContentType.JSON)
                .body(data).
                when()
                .get(TRELLO_URL + "members/me/boards")
                .then()
                .statusCode(200)
                .log()
                .body()
                .extract()
                .response();

        JsonPath js = res.jsonPath();
        List<String> cardIds = js.get("id");
        List<String> values = js.get("name");

        for (int i = 0; i < cardIds.size(); i++) {
            if (cardIds.get(i).contains("Patil")) {
                cardId = values.get(i);
                break;
            }
        }
    }

    /**
     * User validates card details
     */
    public void getCardDetails() {
        click(BOARD_TITLE_NAME);
        HashMap data = new HashMap();
        data.put("key", KEY);
        data.put("token", TOKEN);
        data.put("idList", "637b9abbd52a4f00754a5d51");
        data.put("name", NAME);

        given()
                .contentType(ContentType.JSON)
                .body(data).
                when()
                .post(TRELLO_URL + "cards")
                .then()
                .statusCode(200)
                .log()
                .body()
                .extract()
                .response();
    }

    /**
     * User validates the card message
     */
    public String validCardMessage() {
        return findElementByVisibility
                (By.xpath(
                        "//*[@class='list-card-title js-card-name' and text()='Hello Trello!!']"))
                .getText();
    }
}
