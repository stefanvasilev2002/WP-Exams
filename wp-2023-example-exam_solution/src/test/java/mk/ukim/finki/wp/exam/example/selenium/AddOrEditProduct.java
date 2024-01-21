package mk.ukim.finki.wp.exam.example.selenium;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddOrEditProduct extends AbstractPage {

    private WebElement name;
    private WebElement price;
    private WebElement quantity;
    private WebElement categories;
    private WebElement submit;

    public AddOrEditProduct(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage add(WebDriver driver, String addPath, String name, String price, String quantity, String[] categories) {
        get(driver, addPath);
        AbstractPage.assertRelativeUrl(driver, addPath);
        AddOrEditProduct addOrEditProduct = PageFactory.initElements(driver, AddOrEditProduct.class);
        addOrEditProduct.name.sendKeys(name);
        addOrEditProduct.price.sendKeys(price);
        addOrEditProduct.quantity.sendKeys(quantity);
        Select select = new Select(addOrEditProduct.categories);
        for (String c : categories) {
            select.selectByValue(c);
        }
        addOrEditProduct.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage update(WebDriver driver, WebElement editButton, String name, String price, String quantity, String[] categories) {
        String href = editButton.getAttribute("href");
        System.out.println(href);
        editButton.click();
        AbstractPage.assertAbsoluteUrl(driver, href);

        AddOrEditProduct addOrEditProduct = PageFactory.initElements(driver, AddOrEditProduct.class);
        addOrEditProduct.name.clear();
        addOrEditProduct.name.sendKeys(name);
        addOrEditProduct.price.sendKeys(price);
        addOrEditProduct.quantity.sendKeys(quantity);
        Select select = new Select(addOrEditProduct.categories);
        select.deselectAll();
        for (String c : categories) {
            select.selectByValue(c);
        }
        addOrEditProduct.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }


}