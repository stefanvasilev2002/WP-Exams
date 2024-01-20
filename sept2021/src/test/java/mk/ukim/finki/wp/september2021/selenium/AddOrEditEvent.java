package mk.ukim.finki.wp.september2021.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddOrEditEvent extends AbstractPage {

    private WebElement name;
    private WebElement description;
    private WebElement price;
    private WebElement type;
    private WebElement category;
    private WebElement submit;

    public AddOrEditEvent(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage add(WebDriver driver, String addPath, String name, String description, String price, String type, String location) {
        get(driver, addPath);
        assertRelativeUrl(driver, addPath);

        AddOrEditEvent addOrEditEvent = PageFactory.initElements(driver, AddOrEditEvent.class);
        addOrEditEvent.assertNoError();
        addOrEditEvent.name.sendKeys(name);
        addOrEditEvent.description.sendKeys(description);
        addOrEditEvent.price.sendKeys(price);

        Select selectType = new Select(addOrEditEvent.type);
        selectType.selectByValue(type);

        Select selectLocation = new Select(addOrEditEvent.category);
        selectLocation.selectByValue(location);

        addOrEditEvent.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage update(WebDriver driver, WebElement editButton, String name, String description, String price, String type, String location) {
        String href = editButton.getAttribute("href");
        System.out.println(href);
        editButton.click();
        assertAbsoluteUrl(driver, href);

        AddOrEditEvent addOrEditEvent = PageFactory.initElements(driver, AddOrEditEvent.class);
        addOrEditEvent.name.clear();
        addOrEditEvent.name.sendKeys(name);
        addOrEditEvent.description.sendKeys(description);
        addOrEditEvent.price.sendKeys(price);

        Select selectType = new Select(addOrEditEvent.type);
        selectType.selectByValue(type);

        Select selectLocation = new Select(addOrEditEvent.category);
        selectLocation.selectByValue(location);

        addOrEditEvent.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }
}