package mk.ukim.finki.wp.jan2023.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddOrEditCandidate extends AbstractPage {

    private WebElement name;
    private WebElement bio;
    private WebElement dateOfBirth;
    private WebElement gender;
    private WebElement party;
    private WebElement submit;

    public AddOrEditCandidate(WebDriver driver) {
        super(driver);
    }

    public static ItemsPage add(WebDriver driver, String addPath, String name, String bio, String dateOfBirth, String gender, String party) {
        get(driver, addPath);
        assertRelativeUrl(driver, addPath);

        AddOrEditCandidate addOrEditCandidate = PageFactory.initElements(driver, AddOrEditCandidate.class);
        addOrEditCandidate.assertNoError();
        addOrEditCandidate.name.sendKeys(name);
        addOrEditCandidate.bio.sendKeys(bio);
        addOrEditCandidate.dateOfBirth.sendKeys(dateOfBirth);

        Select selectType = new Select(addOrEditCandidate.gender);
        selectType.selectByValue(gender);

        Select selectLocation = new Select(addOrEditCandidate.party);
        selectLocation.selectByValue(party);

        addOrEditCandidate.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }

    public static ItemsPage update(WebDriver driver, WebElement editButton, String name, String bio, String dateOfBirth, String gender, String party) {
        String href = editButton.getAttribute("href");
        System.out.println(href);
        editButton.click();
        assertAbsoluteUrl(driver, href);

        AddOrEditCandidate addOrEditCandidate = PageFactory.initElements(driver, AddOrEditCandidate.class);
        addOrEditCandidate.name.clear();
        addOrEditCandidate.name.sendKeys(name);
        addOrEditCandidate.bio.clear();
        addOrEditCandidate.bio.sendKeys(bio);
        addOrEditCandidate.dateOfBirth.clear();
        addOrEditCandidate.dateOfBirth.sendKeys(dateOfBirth);

        Select selectType = new Select(addOrEditCandidate.gender);
        selectType.selectByValue(gender);

        Select selectLocation = new Select(addOrEditCandidate.party);
        selectLocation.selectByValue(party);

        addOrEditCandidate.submit.click();
        return PageFactory.initElements(driver, ItemsPage.class);
    }
}