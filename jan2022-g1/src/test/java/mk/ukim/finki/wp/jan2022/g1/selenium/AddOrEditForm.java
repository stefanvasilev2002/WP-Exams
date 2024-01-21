package mk.ukim.finki.wp.jan2022.g1.selenium;

import mk.ukim.finki.wp.exam.util.ExamAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class AddOrEditForm extends AbstractPage {

    private WebElement f1;
    private WebElement f2;
    private WebElement f3;
    private WebElement f4;
    private WebElement f5;
    private WebElement f6;
    private WebElement submit;

    public AddOrEditForm(WebDriver driver) {
        super(driver);
    }


    public static ItemsPage add(WebDriver driver, String addUrl, WebElement addButton, String f1, String f2, String f3, String f4, String f5, String f6) {
        if (addButton != null) {
            addButton.click();
        } else {
            get(driver, addUrl);
        }
        assertAbsoluteUrl(driver, relativeToAbsolute(addUrl));

        AddOrEditForm addOrEditForm = PageFactory.initElements(driver, AddOrEditForm.class);
        addOrEditForm.assertNoError();
        addOrEditForm.f1.sendKeys(f1);
        addOrEditForm.f2.sendKeys(f2);
//        addOrEditForm.f3.sendKeys(f3);

        Select selectF4 = new Select(addOrEditForm.f4);
        List<WebElement> f4Options = selectF4.getOptions();
        boolean populatedOptions = ExamAssert.assertNotEquals("empty f4 options", 0, f4Options.size());
        if (!populatedOptions) {
            selectF4.selectByValue(f4);
        }

        String[] parts = f5.split(",");
        Select selectF5 = new Select(addOrEditForm.f5);
        List<WebElement> f5Options = selectF5.getOptions();
        boolean populatedF5Options = ExamAssert.assertNotEquals("empty f5 options", 0, f5Options.size());
        if (populatedF5Options) {
            for (String p : parts)
                selectF5.selectByValue(p);
        }

        addOrEditForm.f6.sendKeys(f6);

        addOrEditForm.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage update(WebDriver driver, String editUrl, WebElement editButton, String f1, String f2, String f3, String f4, String f5, String f6) {
        String href = editButton.getAttribute("href");
        System.out.println(href);
        editButton.click();
        assertAbsoluteUrl(driver, href);

        AddOrEditForm addOrEditForm = PageFactory.initElements(driver, AddOrEditForm.class);
        ExamAssert.assertNotEmpty("check name not empty", addOrEditForm.f1.getAttribute("value"));
        ExamAssert.assertNotEmpty("check email not empty", addOrEditForm.f2.getAttribute("value"));

        addOrEditForm.f1.clear();
        addOrEditForm.f2.clear();
//        addOrEditForm.f3.clear();
        addOrEditForm.f4.clear();
        addOrEditForm.f5.clear();
        addOrEditForm.f6.clear();

        addOrEditForm.f1.sendKeys(f1);
        addOrEditForm.f2.sendKeys(f2);
//        addOrEditForm.f3.sendKeys(f3);

        Select selectF4 = new Select(addOrEditForm.f4);
        List<WebElement> f4Options = selectF4.getOptions();
        boolean populatedOptions = ExamAssert.assertNotEquals("empty f4 options", 0, f4Options.size());
        if (!populatedOptions) {
            selectF4.selectByValue(f4);
        }

        Select selectF5 = new Select(addOrEditForm.f5);
        List<WebElement> f5Opts = selectF5.getOptions();
        int numSelected = 0;
        for (WebElement opt : f5Opts) {
            numSelected += (opt.isSelected() ? 1 : 0);
        }
        boolean valid = ExamAssert.assertEquals("selected skills in edit form", 2, numSelected);
        if (valid) {
            selectF5.deselectAll();
            String[] parts = f5.split(",");
            for (String p : parts)
                selectF5.selectByValue(p);
        }

        addOrEditForm.f6.sendKeys(f6);

        addOrEditForm.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }
}