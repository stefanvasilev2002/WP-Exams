package mk.ukim.finki.wp.exam.example.selenium;

import mk.ukim.finki.wp.exam.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


public class ItemsPage extends AbstractPage {

    private WebElement nameSearch;

    private WebElement categoryId;

    private WebElement filter;

    @FindBy(css = "tr[class=item]")
    private List<WebElement> productRows;


    @FindBy(css = ".delete-item")
    private List<WebElement> deleteButtons;


    @FindBy(className = "edit-item")
    private List<WebElement> editButtons;


    @FindBy(css = ".add-item")
    private List<WebElement> addProductButton;

    public ItemsPage(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage to(WebDriver driver) {
        get(driver, "/");
        return PageFactory.initElements(driver, ItemsPage.class);
    }


    public ItemsPage filter(String name, String categoryId) {
        System.out.println(driver.getCurrentUrl());
        this.nameSearch.sendKeys(name);
        Select select = new Select(this.categoryId);
        select.selectByValue(categoryId);
        this.filter.click();
        String url = "/?nameSearch=" + name + "&categoryId=" + categoryId;
        AbstractPage.assertRelativeUrl(this.driver, url.replaceAll(" ", "+"));
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public void assertButtons(int deleteButtonsCount, int editButtonsCount, int addButtonsCount) {
        ExamAssert.assertEquals("delete btn count", deleteButtonsCount, this.getDeleteButtons().size());
        ExamAssert.assertEquals("edit btn count", editButtonsCount, this.getEditButtons().size());
        ExamAssert.assertEquals("add btn count", addButtonsCount, this.getAddProductButton().size());
    }

    public void assertItems(int expectedItemsNumber) {
        ExamAssert.assertEquals("Number of items", expectedItemsNumber, this.getProductRows().size());
    }

    public WebElement getNameSearch() {
        return nameSearch;
    }

    public WebElement getCategoryId() {
        return categoryId;
    }

    public WebElement getFilter() {
        return filter;
    }

    public List<WebElement> getProductRows() {
        return productRows;
    }

    public List<WebElement> getDeleteButtons() {
        return deleteButtons;
    }

    public List<WebElement> getEditButtons() {
        return editButtons;
    }

    public List<WebElement> getAddProductButton() {
        return addProductButton;
    }
}
