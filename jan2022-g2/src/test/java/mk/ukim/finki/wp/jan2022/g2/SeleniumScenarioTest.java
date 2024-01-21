package mk.ukim.finki.wp.jan2022.g2;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wp.exam.util.CodeExtractor;
import mk.ukim.finki.wp.exam.util.ExamAssert;
import mk.ukim.finki.wp.exam.util.SubmissionHelper;
import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.selenium.AbstractPage;
import mk.ukim.finki.wp.jan2022.g2.selenium.AddOrEditForm;
import mk.ukim.finki.wp.jan2022.g2.selenium.ItemsPage;
import mk.ukim.finki.wp.jan2022.g2.selenium.LoginPage;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {

    static {
        SubmissionHelper.exam = "2022-jan-g2";
        SubmissionHelper.index = "TODO";
    }

    @Autowired
    UserService userService;

    @Autowired
    DiscussionService service;

    @Order(1)
    @Test
    public void test_list_15pt() {
        SubmissionHelper.startTest("test-list-15");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();

        ExamAssert.assertNotEquals("Empty db", 0, itemNum);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertItems(itemNum, itemNum * 2);

        SubmissionHelper.endTest();
    }

    @Order(2)
    @Test
    public void test_filter_5pt() {
        SubmissionHelper.startTest("test-filter-5");
        ItemsPage listPage = ItemsPage.to(this.driver);

        listPage.filter("", "");
        listPage.assertItems(10, 20);

        listPage.filter("8", "3");
        listPage.assertItems(2, 4);

        listPage.filter("", "3");
        listPage.assertItems(4, 8);

        listPage.filter("8", "");
        listPage.assertItems(4, 8);

        SubmissionHelper.endTest();
    }

    @Order(3)
    @Test
    public void test_filter_service_5pt() {
        SubmissionHelper.startTest("test-filter-service-5");

        ExamAssert.assertEquals("without filter", 10, this.service.filter(null, null).size());
        ExamAssert.assertEquals("by participant", 4, this.service.filter(3L, null).size());
        ExamAssert.assertEquals("by closing before", 4, this.service.filter(null, 8).size());
        ExamAssert.assertEquals("by all", 2, this.service.filter(3L, 8).size());

        SubmissionHelper.endTest();
    }

    @Order(4)
    @Test
    public void test_create_10pt() {
        SubmissionHelper.startTest("test-create-10");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();
        ItemsPage listPage = null;

        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, adminPassword);
        } catch (Exception e) {
            System.err.println("NO LOGIN");
        }
        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        WebElement addButton = listPage.getAddButton().isEmpty() ? null : listPage.getAddButton().get(0);
        String date = LocalDate.now().plusDays(5).toString();

        listPage = AddOrEditForm.add(this.driver, ADD_URL, addButton,
                "f1", "f2", "f3", DiscussionTag.OTHER.name(), "1", date);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        listPage.assertNoError();
        listPage.assertItems(itemNum + 1, 2 * itemNum + 1);

        SubmissionHelper.endTest();
    }

    @Order(5)
    @Test
    public void test_create_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-create-mvc-10");
        List<User> users = this.userService.listAll();
        List<Discussion> discussions = this.service.listAll();

        int itemNum = discussions.size();

        MockHttpServletRequestBuilder addForm = MockMvcRequestBuilders
                .get(ADD_URL);

        this.mockMvc.perform(addForm)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));

        MockHttpServletRequestBuilder addRequest = MockMvcRequestBuilders
                .post(LIST_URL)
                .param("title", "testName")
                .param("description", "testDescription")
                .param("discussionTag", DiscussionTag.OTHER.name())
                .param("participants", users.get(0).getId().toString())
                .param("participants", users.get(1).getId().toString())
                .param("dueDate", LocalDate.now().plusDays(5).toString());

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        discussions = this.service.listAll();
        ExamAssert.assertEquals("Number of items", itemNum + 1, discussions.size());

        SubmissionHelper.endTest();
    }


    @Order(6)
    @Test
    public void test_delete_mvc_5pt() throws Exception {
        SubmissionHelper.startTest("test-delete-5");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();
        Long deleteId = entities.get(itemNum - 1).getId();

        MockHttpServletRequestBuilder deleteRequest = MockMvcRequestBuilders
                .post(LIST_URL + "/" + deleteId + "/delete");

        this.mockMvc.perform(deleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.service.listAll();
        ExamAssert.assertEquals("Number of items", itemNum - 1, entities.size());

        SubmissionHelper.endTest();
    }


    @Order(7)
    @Test
    public void test_delete_5pt() throws Exception {
        SubmissionHelper.startTest("test-delete-5");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, adminPassword);
        } catch (Exception e) {
            System.err.println("NO LOGIN");
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        boolean visibleDeleteButtons = ExamAssert.assertEquals("delete btn count", listPage.getDeleteButtons().size(), itemNum);
        if (visibleDeleteButtons) {
            listPage.getDeleteButtons().get(itemNum - 1).click();
            listPage.assertNoError();

            AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
            listPage.assertItems(itemNum - 1, 2 * (itemNum - 1));
        }

        SubmissionHelper.endTest();
    }

    @Order(8)
    @Test
    public void test_edit_10pt() {
        SubmissionHelper.startTest("test-edit-10");
        List<Discussion> entities = this.service.listAll();

        int itemNum = entities.size();
        Long id = entities.get(0).getId();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, adminPassword);
        } catch (Exception e) {
            System.err.println("NO LOGIN");
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        String editUrl = LIST_URL + "/" + id + "/edit";

        listPage = AddOrEditForm.update(this.driver, editUrl, listPage.getEditButtons().get(0),
                "f1u", "f2u", "f3u", DiscussionTag.OTHER.name(), "1,2,3", LocalDate.now().minusYears(5).toString());
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        if (listPage.assertItems(itemNum, 2 * itemNum + 1)) {
            ExamAssert.assertEquals("The updated entity name is not as expected.", "f1u",
                    listPage.getRows().get(0).findElements(By.tagName("td")).get(0).getText().trim());
        }

        SubmissionHelper.endTest();
    }


    @Order(9)
    @Test
    public void test_edit_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-edit-mvc-10");
        List<Discussion> entities = this.service.listAll();
        Long id = entities.get(0).getId();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder editForm = MockMvcRequestBuilders
                .get(LIST_URL + "/" + id + "/edit");

        this.mockMvc.perform(editForm)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));

        MockHttpServletRequestBuilder editRequest = MockMvcRequestBuilders
                .post(LIST_URL + "/" + id)
                .param("title", "testTitle")
                .param("description", "testDescription")
                .param("discussionTag", DiscussionTag.OTHER.name())
                .param("participants", "1")
                .param("participants", "2")
                .param("participants", "3");

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.service.listAll();
        ExamAssert.assertEquals("Number of items", itemNum, entities.size());
        ExamAssert.assertEquals("The updated entity name is not as expected.", "testTitle", entities.get(0).getTitle());
        ExamAssert.assertEquals("The updated entity participants number is not as expected.", 3, entities.get(0).getParticipants().size());


        SubmissionHelper.endTest();
    }


    @Order(10)
    @Test
    public void test_security_urls_10pt() {
        SubmissionHelper.startTest("test-security-urls-10");
        List<Discussion> entities = this.service.listAll();

        Long id = entities.get(0).getId();
        String editUrl = LIST_URL + "/" + id + "/edit";

        ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        ExamAssert.assertEquals("Has configuration?", "", "");
        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, "/random");
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);

        ExamAssert.assertEquals("Admin login success?", "", "");
        LoginPage loginPage = LoginPage.openLogin(this.driver);
        LoginPage.doLogin(this.driver, loginPage, admin, adminPassword);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, ADD_URL);

        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, editUrl);

        ExamAssert.assertEquals("Logout success url?", "", "");
        LoginPage.logout(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        ExamAssert.assertEquals("Invalid login message?", "", "");
        loginPage = LoginPage.openLogin(this.driver);
        LoginPage.doLogin(this.driver, loginPage, "invalid", "invalid");
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL + "?error=BadCredentials");

        SubmissionHelper.endTest();
    }

    @Order(11)
    @Test
    public void test_security_buttons_10pt() {
        SubmissionHelper.startTest("test-security-buttons-10");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();

        LoginPage.logout(this.driver);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertButtons(0, 0, 0, 0);

        LoginPage loginPage1 = LoginPage.openLogin(this.driver);
        listPage = LoginPage.doLogin(this.driver, loginPage1, admin, adminPassword);
        listPage.assertButtons(itemNum, itemNum, 1, 0);
        LoginPage.logout(this.driver);

        LoginPage loginPage2 = LoginPage.openLogin(this.driver);
        listPage = LoginPage.doLogin(this.driver, loginPage2, user, userPassword);
        listPage.assertButtons(0, 0, 0, itemNum);
        LoginPage.logout(this.driver);
        SubmissionHelper.endTest();
    }


    @Order(12)
    @Test
    public void test_mark_popular_5pt() {
        SubmissionHelper.startTest("test-mark-popular-5");
        List<Discussion> entities = this.service.listAll();
        int itemNum = entities.size();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, adminPassword);
        } catch (Exception e) {
            System.err.println("NO LOGIN");
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        if (ExamAssert.assertEquals("Mark popular before", itemNum, listPage.getChangeItemButtons().size())) {
            listPage.getChangeItemButtons().get(0).click();
            listPage.assertNoError();
            AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
            listPage = PageFactory.initElements(driver, ItemsPage.class);
            ExamAssert.assertEquals("Mark popular after", itemNum - 1, listPage.getChangeItemButtons().size());
        }

        SubmissionHelper.endTest();

    }

    private HtmlUnitDriver driver;
    private MockMvc mockMvc;

    private static String admin = "user0";
    private static String adminPassword = "pass0";
    private static String user = "user1";
    private static String userPassword = "pass1";

    @BeforeEach
    private void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.driver = new HtmlUnitDriver(true);
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    @AfterAll
    public static void finializeAndSubmit() throws JsonProcessingException {
        CodeExtractor.submitSourcesAndLogs();
    }

    public static final String LIST_URL = "/discussions";
    public static final String ADD_URL = "/discussions/add";
    public static final String LOGIN_URL = "/login";

    static class ViewMatcher implements Matcher<String> {

        final String baseName;

        ViewMatcher(String baseName) {
            this.baseName = baseName;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String s = (String) o;
                return s.startsWith(baseName);
            }
            return false;
        }

        @Override
        public void describeMismatch(Object o, Description description) {
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}

