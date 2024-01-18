package mk.ukim.finki.wp.eshop.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AddOrEditProduct extends AbstractPage {

    private WebElement name;
    private WebElement price;
    private WebElement quantity;
    private WebElement category;
    private WebElement manufacturer;
    private WebElement submit;

    public AddOrEditProduct(WebDriver driver) {
        super(driver);
    }

    public static ProductsPage addProduct(WebDriver driver, String name, String price, String quantity, String category, String manufacturer) {
        get(driver, "/products/add-form");
        AddOrEditProduct addOrEditProduct = PageFactory.initElements(driver, AddOrEditProduct.class);
        addOrEditProduct.name.sendKeys(name);
        addOrEditProduct.price.sendKeys(price);
        addOrEditProduct.quantity.sendKeys(quantity);
        addOrEditProduct.category.click();
        addOrEditProduct.category.findElement(By.xpath("//option[. = '" + category + "']")).click();
        addOrEditProduct.manufacturer.click();
        addOrEditProduct.manufacturer.findElement(By.xpath("//option[. = '" + manufacturer + "']")).click();

        addOrEditProduct.submit.click();
        return PageFactory.initElements(driver, ProductsPage.class);
    }

    public static ProductsPage editProduct(WebDriver driver, WebElement editButton, String name, String price, String quantity, String category, String manufacturer) {
        editButton.click();
        System.out.println(driver.getCurrentUrl());
        AddOrEditProduct addOrEditProduct = PageFactory.initElements(driver, AddOrEditProduct.class);
        addOrEditProduct.name.sendKeys(name);
        addOrEditProduct.price.sendKeys(price);
        addOrEditProduct.quantity.sendKeys(quantity);
        addOrEditProduct.category.click();
        addOrEditProduct.category.findElement(By.xpath("//option[. = '" + category + "']")).click();
        addOrEditProduct.manufacturer.click();
        addOrEditProduct.manufacturer.findElement(By.xpath("//option[. = '" + manufacturer + "']")).click();

        addOrEditProduct.submit.click();
        return PageFactory.initElements(driver, ProductsPage.class);
    }


}