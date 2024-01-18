package mk.ukim.finki.wp.september2021.selenium;

import mk.ukim.finki.wp.exam.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ItemsPage extends AbstractPage {

    private WebElement price;

    private WebElement type;

    private WebElement filter;

    @FindBy(css = "tr[class=item]")
    private List<WebElement> eventsRows;

    @FindBy(css = ".delete-item")
    private List<WebElement> deleteButtons;

    @FindBy(className = "edit-item")
    private List<WebElement> editButtons;

    @FindBy(css = ".like-item")
    private List<WebElement> likeButtons;

    @FindBy(css = ".add-item")
    private List<WebElement> addEventButton;

    public ItemsPage(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage to(WebDriver driver) {
        get(driver, "/");
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public ItemsPage filter(String price, String type) {
        System.out.println(driver.getCurrentUrl());
        this.price.sendKeys(price);
        Select select = new Select(this.type);
        select.selectByValue(type);
        this.filter.click();
        String url = "/?price=" + price + "&type=" + type;
        assertRelativeUrl(this.driver, url.replaceAll(" ", "+"));
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public void assertButtons(int deleteButtonsCount, int editButtonsCount, int addButtonsCount, int likeButtonsCount) {
        ExamAssert.assertEquals("delete btn count", deleteButtonsCount, this.getDeleteButtons().size());
        ExamAssert.assertEquals("edit btn count", editButtonsCount, this.getEditButtons().size());
        ExamAssert.assertEquals("add btn count", addButtonsCount, this.getAddEventButton().size());
        ExamAssert.assertEquals("follow btn count", likeButtonsCount, this.getLikeButton().size());
    }

    public boolean assertItems(int expectedItemsNumber) {
        return ExamAssert.assertEquals("Number of items", expectedItemsNumber, this.getEventsRows().size());
    }


    public WebElement getPrice() {
        return price;
    }

    public WebElement getType() {
        return type;
    }

    public WebElement getFilter() {
        return filter;
    }

    public List<WebElement> getEventsRows() {
        return eventsRows;
    }

    public List<WebElement> getDeleteButtons() {
        return deleteButtons;
    }

    public List<WebElement> getEditButtons() {
        return editButtons;
    }

    public List<WebElement> getLikeButton() {
        return likeButtons;
    }

    public List<WebElement> getAddEventButton() {
        return addEventButton;
    }
}
