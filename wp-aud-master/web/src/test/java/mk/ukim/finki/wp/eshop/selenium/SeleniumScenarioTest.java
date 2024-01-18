package mk.ukim.finki.wp.eshop.selenium;


import mk.ukim.finki.wp.eshop.model.*;
import mk.ukim.finki.wp.eshop.service.CategoryService;
import mk.ukim.finki.wp.eshop.service.ManufacturerService;
import mk.ukim.finki.wp.eshop.service.ProductService;
import mk.ukim.finki.wp.eshop.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {


    @Autowired
    UserService userService;

    @Autowired
    ManufacturerService manufacturerService;

    @Autowired
    CategoryService categoryService;


    @Autowired
    ProductService productService;


    private HtmlUnitDriver driver;

    private static Category c1;
    private static Category c2;
    private static Manufacturer m1;
    private static Manufacturer m2;
    private static User regularUser;
    private static User adminUser;

    private static String user = "user";
    private static String admin = "admin";

    private static boolean dataInitialized = false;


    @BeforeEach
    private void setup() {
        this.driver = new HtmlUnitDriver(true);
        initData();
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }


    private void initData() {
        if (!dataInitialized) {
            c1 = categoryService.create("c1", "c1");
            c2 = categoryService.create("c2", "c2");

            m1 = manufacturerService.save("m1", "m1").get();
            m2 = manufacturerService.save("m2", "m2").get();


            regularUser = userService.register(user, user, user, user, user, Role.ROLE_USER);
            adminUser = userService.register(admin, admin, admin, admin, admin, Role.ROLE_ADMIN);
            dataInitialized = true;
        }
    }

    @Test
    public void testScenario() throws Exception {
        ProductsPage productsPage = ProductsPage.to(this.driver);
        productsPage.assertElemts(0, 0, 0, 0, 0);
        LoginPage loginPage = LoginPage.openLogin(this.driver);

        productsPage = LoginPage.doLogin(this.driver, loginPage, adminUser.getUsername(), admin);
        productsPage.assertElemts(0, 0, 0, 0, 1);

        productsPage = AddOrEditProduct.addProduct(this.driver, "test", "100", "5", c2.getName(), m2.getName());
        productsPage.assertElemts(1, 1, 1, 1, 1);

        productsPage = AddOrEditProduct.addProduct(this.driver, "test1", "200", "4", c1.getName(), m2.getName());
        productsPage.assertElemts(2, 2, 2, 2, 1);

        productsPage.getDeleteButtons().get(1).click();
        productsPage.assertElemts(1, 1, 1, 1, 1);

        productsPage = AddOrEditProduct.editProduct(this.driver, productsPage.getEditButtons().get(0), "test1", "200", "4", c1.getName(), m2.getName());
        productsPage.assertElemts(1, 1, 1, 1, 1);

        loginPage = LoginPage.logout(this.driver);
        productsPage = LoginPage.doLogin(this.driver, loginPage, regularUser.getUsername(), user);
        productsPage.assertElemts(1, 0, 0, 1, 0);

        productsPage.getCartButtons().get(0).click();
        Assert.assertEquals("url do not match", "http://localhost:9999/shopping-cart", this.driver.getCurrentUrl());


        ShoppingCartPage cartPage = ShoppingCartPage.init(this.driver);
        cartPage.assertElemts(1);


    }


}
