package com.qa.ui.tests;

import net.jodah.failsafe.internal.util.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.CONCURRENT)
public class HomePageTest extends BaseTest {

    @BeforeEach
    public void navigateAndSearch() {
        homePage.navigateHomePage();
        homePage.login("tinapatil879@gmail.com", "tina879@");
    }

    @Test
    public void validateBoard() {
        homePage.createBoard("Solutions");
        Assert.isTrue(homePage.validateBoardTitle(), "The board is not created");
    }

    @Test
    public void validateCard() {
        homePage.getCardIds();
        homePage.getCardDetails();
        Assert.isTrue(homePage.validCardMessage()
                .equalsIgnoreCase("Hello Trello!!"), "Incorrect card message");
    }
}
