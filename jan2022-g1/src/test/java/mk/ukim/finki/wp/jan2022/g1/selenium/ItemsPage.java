package mk.ukim.finki.wp.jan2022.g1.selenium;

import mk.ukim.finki.wp.exam.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ItemsPage extends AbstractPage {

    private WebElement f1;

    private WebElement f2;

    private WebElement filter;

    @FindBy(css = "tr[class=item]")
    private List<WebElement> rows;

    @FindBy(css = ".item-rel")
    private List<WebElement> rels;

    @FindBy(css = ".delete-item")
    private List<WebElement> deleteButtons;

    @FindBy(className = "edit-item")
    private List<WebElement> editButtons;

    @FindBy(css = ".add-item")
    private List<WebElement> addButton;

    @FindBy(css = ".change-item")
    private List<WebElement> changeItemButtons;


    public ItemsPage(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage to(WebDriver driver) {
        get(driver, "/");
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public ItemsPage filter(String f1, String f2) {
        System.out.println(driver.getCurrentUrl());
        this.f1.sendKeys(f1);
        Select select = new Select(this.f2);
        select.selectByValue(f2);
        this.filter.click();
        String url = "/?lessThanDayBeforeDueDate=" + f1 + "&assigneeId=" + f2;
        assertRelativeUrl(this.driver, url.replaceAll(" ", "+"));
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public void assertButtons(int deleteButtonsCount, int editButtonsCount, int addButtonsCount, int likeButtonsCount) {
        int actualDelete = 0;
        int actualEdit = 0;
        int actualAdd = 0;
        try {
            actualDelete = this.getDeleteButtons().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            actualEdit = this.getEditButtons().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            actualAdd = this.getAddButton().size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExamAssert.assertEquals("delete btn count", deleteButtonsCount, actualDelete);
        ExamAssert.assertEquals("edit btn count", editButtonsCount, actualEdit);
        ExamAssert.assertEquals("add btn count", addButtonsCount, actualAdd);
    }

    public boolean assertItems(int expectedItemsNumber, int expectedRelNumber) {
        return ExamAssert.assertEquals("Number of items", expectedItemsNumber, this.getRows().size()) &&
                ExamAssert.assertEquals("Number of assignees", expectedRelNumber, this.rels.size());
    }


    public WebElement getF1() {
        return f1;
    }

    public WebElement getF2() {
        return f2;
    }

    public WebElement getFilter() {
        return filter;
    }

    public List<WebElement> getRows() {
        return rows;
    }

    public List<WebElement> getDeleteButtons() {
        return deleteButtons;
    }

    public List<WebElement> getEditButtons() {
        return editButtons;
    }

    public List<WebElement> getAddButton() {
        return addButton;
    }

    public List<WebElement> getChangeItemButtons() {
        return changeItemButtons;
    }
}
