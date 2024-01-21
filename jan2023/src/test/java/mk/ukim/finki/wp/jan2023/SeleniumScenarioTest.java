package mk.ukim.finki.wp.jan2023;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wp.exam.util.CodeExtractor;
import mk.ukim.finki.wp.jan2023.selenium.AbstractPage;
import mk.ukim.finki.wp.jan2023.selenium.AddOrEditCandidate;
import mk.ukim.finki.wp.jan2023.selenium.ItemsPage;
import mk.ukim.finki.wp.jan2023.selenium.LoginPage;
import mk.ukim.finki.wp.jan2023.service.CandidateService;
import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Party;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.service.PartyService;
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

import java.time.LocalDate;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumScenarioTest {

    static {
        SubmissionHelper.exam = "wp-jan2023";
        SubmissionHelper.index = "TODO";
    }

    @Autowired
    PartyService partyService;

    @Autowired
    CandidateService candidateService;

    @Order(1)
    @Test
    public void test_list_15pt() {
        SubmissionHelper.startTest("test-list-15");
        List<Candidate> candidates = this.candidateService.listAllCandidates();
        int itemNum = candidates.size();

        ExamAssert.assertNotEquals("Empty db", 0, itemNum);

        ItemsPage listPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        listPage.assertItems(itemNum);

        SubmissionHelper.endTest();
    }

    @Order(2)
    @Test
    public void test_filter_5pt() {
        SubmissionHelper.startTest("test-filter-5");
        ItemsPage listPage = ItemsPage.to(this.driver);

        listPage.filter("", "");
        listPage.assertItems(10);

        listPage.filter("30", "");
        listPage.assertItems(5);

        listPage.filter("", Gender.MALE.name());
        listPage.assertItems(5);

        listPage.filter("30", Gender.MALE.name());
        listPage.assertItems(3);

        SubmissionHelper.endTest();
    }

    @Order(3)
    @Test
    public void test_filter_service_5pt() {
        SubmissionHelper.startTest("test-filter-service-5");

        ExamAssert.assertEquals("without filter", 10, this.candidateService.listCandidatesYearsMoreThanAndGender(null, null).size());
        ExamAssert.assertEquals("by years more than only", 5, this.candidateService.listCandidatesYearsMoreThanAndGender(30, null).size());
        ExamAssert.assertEquals("by gender only", 5, this.candidateService.listCandidatesYearsMoreThanAndGender(null, Gender.FEMALE).size());
        ExamAssert.assertEquals("by years less than and gender", 3, this.candidateService.listCandidatesYearsMoreThanAndGender(30, Gender.MALE).size());

        SubmissionHelper.endTest();
    }

    @Order(4)
    @Test
    public void test_create_10pt() {
        SubmissionHelper.startTest("test-create-10");
        List<Party> parties = this.partyService.listAll();
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();
        ItemsPage listPage = null;

        try {
            LoginPage loginPage = LoginPage.openLogin(this.driver);
            listPage = LoginPage.doLogin(this.driver, loginPage, admin, admin);
        } catch (Exception e) {
        }

        String date = LocalDate.now().minusYears(30).toString();

        listPage = AddOrEditCandidate.add(this.driver, ADD_URL, "testName", "testBio", date, Gender.MALE.name(), parties.get(0).getId().toString());
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        listPage.assertNoError();
        listPage.assertItems(itemNum + 1);

        SubmissionHelper.endTest();
    }

    @Order(5)
    @Test
    public void test_create_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-create-mvc-10");
        List<Party> parties = this.partyService.listAll();
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();

        MockHttpServletRequestBuilder addRequest = MockMvcRequestBuilders
                .post("/candidates")
                .param("name", "testName")
                .param("bio", "testBio")
                .param("dateOfBirth", LocalDate.now().minusDays(30).toString())
                .param("gender", Gender.MALE.name())
                .param("party", parties.get(0).getId().toString());

        this.mockMvc.perform(addRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        candidates = this.candidateService.listAllCandidates();
        ExamAssert.assertEquals("Number of items", itemNum + 1, candidates.size());

        addRequest = MockMvcRequestBuilders
                .get("/candidates/add");

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
        List<Party> parties = this.partyService.listAll();
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();

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

        listPage = AddOrEditCandidate.update(this.driver, listPage.getEditButtons().get(itemNum - 1), "testName", "testBio", LocalDate.now().minusYears(30).toString(), Gender.MALE.name(), parties.get(0).getId().toString());
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        if (listPage.assertItems(itemNum)) {
            ExamAssert.assertEquals("The updated candidate name is not as expected.", "testName", listPage.getRows().get(itemNum - 1).findElements(By.tagName("td")).get(0).getText().trim());
        }

        SubmissionHelper.endTest();
    }

    @Order(7)
    @Test
    public void test_edit_mvc_10pt() throws Exception {
        SubmissionHelper.startTest("test-edit-mvc-10");
        List<Party> parties = this.partyService.listAll();
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/candidates/" + candidates.get(itemNum - 1).getId())
                .param("name", "testName")
                .param("bio", "testBio")
                .param("dateOfBirth", LocalDate.now().minusYears(30).toString())
                .param("gender", Gender.FEMALE.name())
                .param("party", parties.get(0).getId().toString());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        candidates = this.candidateService.listAllCandidates();
        ExamAssert.assertEquals("Number of items", itemNum, candidates.size());
        ExamAssert.assertEquals("The updated candidate name is not as expected.", "testName", candidates.get(itemNum - 1).getName());

        request = MockMvcRequestBuilders
                .get("/candidates/" + candidates.get(itemNum - 1).getId() + "/edit");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(new ViewMatcher("form")));

        SubmissionHelper.endTest();
    }

    @Order(8)
    @Test
    public void test_delete_5pt() throws Exception {
        SubmissionHelper.startTest("test-delete-5");
        List<Candidate> candidates = this.candidateService.listAllCandidates();
        int itemNum = candidates.size();

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
        List<Candidate> candidates = this.candidateService.listAllCandidates();
        int itemNum = candidates.size();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/candidates/" + candidates.get(itemNum - 1).getId() + "/delete");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        candidates = this.candidateService.listAllCandidates();
        ExamAssert.assertEquals("Number of items", itemNum - 1, candidates.size());

        SubmissionHelper.endTest();
    }

    @Order(10)
    @Test
    public void test_security_urls_10pt() {
        SubmissionHelper.startTest("test-security-urls-10");
        List<Candidate> candidates = this.candidateService.listAllCandidates();
        String editUrl = "/candidates/" + candidates.get(0).getId() + "/edit";

        ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");

        AbstractPage.get(this.driver, LIST_URL);
        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
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
    public void test_security_buttons_10pt() {
        SubmissionHelper.startTest("test-security-buttons-10");
        List<Candidate> candidates = this.candidateService.listAllCandidates();
        int itemNum = candidates.size();

        ItemsPage candidatesPage = ItemsPage.to(this.driver);
        AbstractPage.assertRelativeUrl(this.driver, "/");
        candidatesPage.assertButtons(0, 0, 0, 0);

        LoginPage loginPage1 = LoginPage.openLogin(this.driver);
        candidatesPage = LoginPage.doLogin(this.driver, loginPage1, admin, admin);
        candidatesPage.assertButtons(itemNum, itemNum, 1, 0);
        LoginPage.logout(this.driver);

        LoginPage loginPage2 = LoginPage.openLogin(this.driver);
        candidatesPage = LoginPage.doLogin(this.driver, loginPage2, user, user);
        candidatesPage.assertButtons(0, 0, 0, itemNum);
        LoginPage.logout(this.driver);
        SubmissionHelper.endTest();
    }

    @Order(12)
    @Test
    public void test_vote_3pt() throws Exception {
        SubmissionHelper.startTest("test-vote-3");
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();

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

        listPage.getVoteButtons().get(itemNum - 1).click();
        listPage.assertNoError();

        AbstractPage.assertRelativeUrl(this.driver, LIST_URL);
        ExamAssert.assertEquals("The updated candidate votes number is not as expected.", "1", listPage.getRows().get(itemNum - 1).findElements(By.tagName("td")).get(5).getText().trim());

        SubmissionHelper.endTest();
    }

    @Order(13)
    @Test
    public void test_vote_mvc_2pt() throws Exception {
        SubmissionHelper.startTest("test-vote-mvc-2");
        List<Candidate> candidates = this.candidateService.listAllCandidates();

        int itemNum = candidates.size();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/candidates/" + candidates.get(0).getId() + "/vote");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LIST_URL));

        candidates = this.candidateService.listAllCandidates();
        ExamAssert.assertEquals("Number of votes", candidates.get(0).getVotes(), 1);

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
    public static void finalizeAndSubmit() throws JsonProcessingException {
        CodeExtractor.submitSourcesAndLogs();
    }

    public static final String LIST_URL = "/candidates";
    public static final String ADD_URL = "/candidates/add";
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
