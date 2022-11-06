package site.metacoding.miniproject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.metacoding.miniproject.domain.company.Company;
import site.metacoding.miniproject.domain.company.CompanyDao;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.domain.user.UserDao;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.dto.request.company.CompanyJoinReqDto;
import site.metacoding.miniproject.dto.request.company.CompanyMyPageUpdateReqDto;
import site.metacoding.miniproject.dto.request.notice.NoticeInsertReqDto;

@ActiveProfiles("test") // 테스트 어플리케이션 실행
@Transactional
@AutoConfigureMockMvc // MockMvc Ioc 컨테이너에 등록 실제가 아닌 가짜
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // MOCK은 가짜 환경임
public class CompanyApiControllerTest {

    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private MockMvc mvc; // 이걸로 통신을 한다

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserDao userDao;

    private MockHttpSession session;

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();// 직접 new를 했다 MockHttpSession해야 Mock가 된다
        User user = User.builder().userId(11).username("empc").role("company").build();
        session.setAttribute("principal", new SessionUserDto(user));
    }

    @BeforeEach
    public void dataInit() {
        Company company = Company.builder().companyName("삼성").companyGoal("1조").userId(11).build();
    }

    // 기업회원가입
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void joinCompany_test() throws Exception {

        // given
        CompanyJoinReqDto companyJoinDto = new CompanyJoinReqDto();
        companyJoinDto.setUsername("asdfdafds");
        companyJoinDto.setPassword("1234");
        companyJoinDto.setRole("person");

        String body = om.writeValueAsString(companyJoinDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/company/join").content(body)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));
        System.out.println("디버그 : " + resultActions.andReturn().getResponse().getContentAsString());
        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 기업 회원가입 페이지
    @Test
    public void companyJoinForm_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/companyJoinForm").accept(APPLICATION_JSON));
        System.out.println("디버그 : " + resultActions.andReturn().getResponse().getContentAsString());

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }

    // 기업추천 리스트 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void recommendListFrom_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/company/recommendListFrom")
                        .accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }

    // 기업 마이페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void companyMyPageForm_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/companyMypageForm").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 기업 마이페이지 수정하기
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void update_test() throws Exception {
        // given
        Long id = 1L;
        CompanyMyPageUpdateReqDto companyMyPageUpdateReqDto = new CompanyMyPageUpdateReqDto();
        companyMyPageUpdateReqDto.setCeoName("나사장");

        String body = om.writeValueAsString(companyMyPageUpdateReqDto);
        // json 변경

        // when
        ResultActions resultActions = mvc
                .perform(put("/api/companyMypage").session(session).content(body)
                        // post안에 , 해서 넣을수있다 -> 쿼리스트림
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON) // 둘 중 하나라도 안적으면 안나옴 -> json타입인줄 몰라서
                        .session(session)); // 가짜세션!!

        // then/ charset=utf-8안넣으면바로한글이깨진다
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(jsonPath("$.code").value(1L));
        resultActions.andExpect(jsonPath("$.data.ceoName").value("나사장"));
    }

    // 기업소개등록 페이지 불러오기
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void companyInsertForm_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/companyInsertWriteForm").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 기업추천리스트보기
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void skillCompanyMatching_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/matchingListFrom").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void skillCompanyMatchingList_test() throws Exception {

        // given
        List<String> skillList = new ArrayList<>();
        skillList.add("java");
        skillList.add("javascript");

        String body = om.writeValueAsString(skillList);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/company/skillCompanyMatchingList/needSkill")
                        .content(body)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));
        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
    }

    // 구독 관리 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void subscribeManage_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/subscribeManageForm").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 구독 취소
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void deleteSubscribe_test() throws Exception {
        // given
        Integer subscribeId = 1;
        // when
        ResultActions resultActions = mvc
                .perform(delete("/company/deleteSubscribe/" + subscribeId)
                        .accept(APPLICATION_JSON)
                        .session(session));

        // then/ charset=utf-8안넣으면바로한글이깨진다

        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(jsonPath("$.code").value(1L));
    }

    // 기업 상세보기 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void companyDetai_test() throws Exception {
        // given
        Integer companyId = 1;

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/companyDetailForm/" + companyId).session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 기업 구독
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void companySubscribe_test() throws Exception {

        // given
        Integer subjectId = 1;

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/company/subscribe/" + subjectId).session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));
        System.out.println("디버그 : " + resultActions.andReturn().getResponse().getContentAsString());
        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 기업 추천
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void companyRecommend_test() throws Exception {

        // given
        Integer subjectId = 1;

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/company/recommend/" + subjectId).session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));
        System.out.println("디버그 : " + resultActions.andReturn().getResponse().getContentAsString());
        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 등록 공고 보기 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void noticeLoad_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/noticeLoadForm").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 공고 등록하기 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void noticeWrite_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/noticeWriteForm").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 공고 등록
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void noticeInsert_test() throws Exception {

        // given

        NoticeInsertReqDto noticeInsertDto = new NoticeInsertReqDto();
        noticeInsertDto.setCompanyId(1);
        noticeInsertDto.setNoticeTitle("들어오asdfasdf세요");
        noticeInsertDto.setDegree("3년차");

        List<String> needSkill = new ArrayList<>();
        needSkill.add("java");
        needSkill.add("javascript");

        noticeInsertDto.setNeedSkill(needSkill);
        String body = om.writeValueAsString(noticeInsertDto);

        // when
        ResultActions resultActions = mvc
                .perform(
                        MockMvcRequestBuilders.post("/company/noticeInsert").session(session).content(body)
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON));
        System.out.println("디버그 : " + resultActions.andReturn().getResponse().getContentAsString());

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 공고 상세보기 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void noticeDetail_test() throws Exception {
        // given
        Integer noticeId = 1;

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/noticeDetailForm/" + noticeId).session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }

    // 공고 상세보기 페이지
    @Sql(scripts = "classpath:create.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void myCompanyDetail_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/company/companyDetail").session(session).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(status().isOk());

    }
}