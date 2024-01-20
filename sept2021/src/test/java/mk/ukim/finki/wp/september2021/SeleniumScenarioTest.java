package mk.ukim.finki.wp.september2021;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wp.exam.util.CodeExtractor;
import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.selenium.AbstractPage;
import mk.ukim.finki.wp.september2021.selenium.AddOrEditEvent;
import mk.ukim.finki.wp.september2021.selenium.ItemsPage;
import mk.ukim.finki.wp.september2021.selenium.LoginPage;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import mk.ukim.finki.wp.exam.util.ExamAssert;
import mk.ukim.finki.wp.exam.util.SubmissionHelper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {

    static {
        SubmissionHelper.exam = "wp-september";
        SubmissionHelper.index = "34504";
    }

    @Autowired
    NewsCategoryService newsCategoryService;

    @Autowired
    NewsService service;

    @Order(1)
    @Test
    public void test_list_20pt() {
        SubmissionHelper.startTest("test-list-20");
        List<News> entities = this.service.listAllNews();
        int itemNum = entities.size();

        ExamAssert.assertNotEquals("Empty db", 0, itemNum);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertItems(itemNum);

        SubmissionHelper.endTest();
    }

    @Order(2)
    @Test
    public void test_filter_10pt() {
        SubmissionHelper.startTest("test-filter-10");
        ItemsPage listPage = ItemsPage.to(this.driver);

        listPage.filter("", "");
        listPage.assertItems(10);

        listPage.filter("45.0", "");
        listPage.assertItems(2);

        listPage.filter("", NewsType.DRAFT.name());
        listPage.assertItems(3);

        listPage.filter("100.0", NewsType.DRAFT.name());
        listPage.assertItems(1);

        SubmissionHelper.endTest();
    }

    @Order(3)
    @Test
    public void test_filter_service_10pt() {
        SubmissionHelper.startTest("test-filter-service-10");

        ExamAssert.assertEquals("without filter", 10, this.service.listNewsWithPriceLessThanAndType(null, null).size());
        ExamAssert.assertEquals("by price less than only", 2, this.service.listNewsWithPriceLessThanAndType(45.0, null).size());
        ExamAssert.assertEquals("by type only", 3, this.service.listNewsWithPriceLessThanAndType(null, NewsType.DRAFT).size());
        ExamAssert.assertEquals("by price less than and type", 1, this.service.listNewsWithPriceLessThanAndType(100.0, NewsType.DRAFT).size());

        SubmissionHelper.endTest();
    }

    @Order(4)
    @Test
    public void test_create_10pt() {
        SubmissionHelper.startTest("test-create-10");
        List<NewsCategory> categories = this.newsCategoryService.listAll();
        List<News> entities = this.service.listAllNews();

        int itemNum = entities.size();
        ItemsPage listPage = null;

        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
        }
        listPage = AddOrEditEvent.add(this.driver, ADD_URL, "testName", "testDescription", "100", NewsType.PROMOTION.name(), categories.get(0).getId().toString());
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        listPage.assertNoError();
        listPage.assertItems(itemNum + 1);

        SubmissionHelper.endTest();
    }

    @Order(5)
    @Test
    public void test_create_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-create-mvc-10");
        List<NewsCategory> categories = this.newsCategoryService.listAll();
        List<News> news = this.service.listAllNews();

        int itemNum = news.size();

        MockHttpServletRequestBuilder addRequest = MockMvcRequestBuilders
                .post("/news")
                .param("name", "testName")
                .param("description", "testDescription")
                .param("price", "45.0")
                .param("type", NewsType.PROMOTION.name())
                .param("category", categories.get(0).getId().toString());

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        news = this.service.listAllNews();
        ExamAssert.assertEquals("Number of items", itemNum + 1, news.size());

        addRequest = MockMvcRequestBuilders
                .get("/news/add");

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));

        SubmissionHelper.endTest();
    }

    @Order(6)
    @Test
    public void test_edit_10pt() {
        SubmissionHelper.startTest("test-edit-10");
        List<NewsCategory> categories = this.newsCategoryService.listAll();
        List<News> entities = this.service.listAllNews();

        int itemNum = entities.size();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        listPage = AddOrEditEvent.update(this.driver, listPage.getEditButtons().get(itemNum - 1), "testEventName", "testEventDescription", "100", NewsType.PROMOTION.name(), categories.get(0).getId().toString());
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        if (listPage.assertItems(itemNum)) {
            ExamAssert.assertEquals("The updated entity name is not as expected.", "testEventName",
                    listPage.getEventsRows().get(itemNum - 1).findElements(By.tagName("td")).get(0).getText().trim());
        }

        SubmissionHelper.endTest();
    }

    @Order(7)
    @Test
    public void test_edit_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-edit-mvc-10");
        List<NewsCategory> categories = this.newsCategoryService.listAll();
        List<News> entities = this.service.listAllNews();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder editRequest = MockMvcRequestBuilders
                .post("/news/" + entities.get(itemNum - 1).getId())
                .param("name", "testEventName")
                .param("description", "testEventDescription")
                .param("price", "100")
                .param("type", NewsType.PROMOTION.name())
                .param("category", categories.get(0).getId().toString());

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.service.listAllNews();
        ExamAssert.assertEquals("Number of items", itemNum, entities.size());
        ExamAssert.assertEquals("The updated entity name is not as expected.", "testEventName", entities.get(itemNum - 1).getName());

        editRequest = MockMvcRequestBuilders
                .get("/news/" + entities.get(itemNum - 1).getId() + "/edit");

        this.mockMvc.perform(editRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));

        SubmissionHelper.endTest();
    }

    @Order(8)
    @Test
    public void test_delete_5pt() throws Exception {
        SubmissionHelper.startTest("test-delete-5");
        List<News> entities = this.service.listAllNews();
        int itemNum = entities.size();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        listPage.getDeleteButtons().get(itemNum - 1).click();
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        listPage.assertItems(itemNum - 1);

        SubmissionHelper.endTest();
    }

    @Order(9)
    @Test
    public void test_delete_mvc_5pt() throws Exception {
        SubmissionHelper.startTest("test-delete-5");
        List<News> entities = this.service.listAllNews();
        int itemNum = entities.size();

        MockHttpServletRequestBuilder deleteRequest = MockMvcRequestBuilders
                .post("/news/" + entities.get(itemNum - 1).getId() + "/delete");

        this.mockMvc.perform(deleteRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.service.listAllNews();
        ExamAssert.assertEquals("Number of items", itemNum - 1, entities.size());

        SubmissionHelper.endTest();
    }

    @Order(10)
    @Test
    public void test_security_urls_7pt() {
        SubmissionHelper.startTest("test-security-urls-7");
        List<News> entities = this.service.listAllNews();
        String editUrl = "/news/" + entities.get(0).getId() + "/edit";

        ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);
        AbstractPage.get(this.driver, "/random");
        AbstractPage.assertRelativeUrl(this.driver, LOGIN_URL);

        LoginPage loginPage = LoginPage.openLogin(this.driver);
        LoginPage.doLogin(this.driver, loginPage, admin, admin);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);

        AbstractPage.get(this.driver, ADD_URL);
        AbstractPage.assertRelativeUrl(this.driver, ADD_URL);

        AbstractPage.get(this.driver, editUrl);
        AbstractPage.assertRelativeUrl(this.driver, editUrl);

        LoginPage.logout(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        SubmissionHelper.endTest();
    }

    @Order(11)
    @Test
    public void test_security_buttons_7pt() {
        SubmissionHelper.startTest("test-security-buttons-7");
        List<News> entities = this.service.listAllNews();
        int itemNum = entities.size();

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertButtons(0, 0, 0, 0);

        LoginPage loginPage1 = LoginPage.openLogin(this.driver);
        listPage = LoginPage.doLogin(this.driver, loginPage1, admin, admin);
        listPage.assertButtons(itemNum, itemNum, 1, 0);
        LoginPage.logout(this.driver);

        LoginPage loginPage2 = LoginPage.openLogin(this.driver);
        listPage = LoginPage.doLogin(this.driver, loginPage2, user, user);
        listPage.assertButtons(0, 0, 0, itemNum);
        LoginPage.logout(this.driver);
        SubmissionHelper.endTest();
    }

    @Order(12)
    @Test
    public void test_like_3pt() throws Exception {
        SubmissionHelper.startTest("test-like-3");
        List<News> entities = this.service.listAllNews();

        int itemNum = entities.size();

        ItemsPage listPage = null;
        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, user, user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!LIST_URL.equals(this.driver.getCurrentUrl())) {
            System.err.println("Reloading items page");
            listPage = ItemsPage.to(this.driver);
        }

        listPage.getLikeButton().get(itemNum - 1).click();
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        ExamAssert.assertEquals("The updated entities likes number is not as expected.", "1",
                listPage.getEventsRows().get(itemNum - 1).findElements(By.tagName("td")).get(5).getText().trim());

        SubmissionHelper.endTest();
    }

    @Order(13)
    @Test
    public void test_like_mvc_3pt() throws Exception {
        SubmissionHelper.startTest("test-like-mvc-3");
        List<News> entities = this.service.listAllNews();

        int itemNum = entities.size();

        MockHttpServletRequestBuilder likeRequest = MockMvcRequestBuilders
                .post("/news/" + entities.get(0).getId() + "/like");

        this.mockMvc.perform(likeRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        entities = this.service.listAllNews();
        ExamAssert.assertEquals("Number of likes", 1, entities.get(0).getLikes());

        SubmissionHelper.endTest();
    }

    private HtmlUnitDriver driver;
    private MockMvc mockMvc;

    private static String admin = "admin";
    private static String user = "user";

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

    public static final String LIST_URL = "/news";
    public static final String ADD_URL = "/news/add";
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

