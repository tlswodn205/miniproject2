package site.metacoding.miniproject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.domain.user.UserDao;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.dto.request.company.CompanyJoinReqDto;
import site.metacoding.miniproject.dto.request.resume.ResumeWriteReqDto;
import site.metacoding.miniproject.dto.request.resume.SubmitResumeReqDto;
import site.metacoding.miniproject.dto.response.person.InterestPersonRespDto;

@ActiveProfiles("test") // 테스트 어플리케이션 실행
@Sql("classpath:truncate.sql")
@Transactional
@AutoConfigureMockMvc // MockMvc Ioc 컨테이너에 등록 실제가 아닌 가짜
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // MOCK은 가짜 환경임
public class PersonApiControllerTest {

    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    @Autowired
    private MockMvc mvc; // 이걸로 통신을 한다

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDao userDao;

    private MockHttpSession session;

    @BeforeEach
    public void sessionInit() {
        session = new MockHttpSession();// 직접 new를 했다 MockHttpSession해야 Mock가 된다
        User user = User.builder().userId(1).username("ssar").build();// password 는 없다
        session.setAttribute("principal", new SessionUserDto(user));// 가짜세션이 만들어진 상태이다 -> 아직 주입은 안된 상태
    }

    // @BeforeEach
    // public void sessionInit() {
    // session = new MockHttpSession();// 직접 new를 했다 MockHttpSession해야 Mock가 된다
    // User user = User.builder().userId(1).username("ssar").build();// password 는
    // 없다
    // session.setAttribute("sessionUserDto", new SessionUserDto(user));// 가짜세션이
    // 만들어진 상태이다 -> 아직 주입은 안된 상태
    // }

    // @BeforeEach
    // public void dataInit() {
    // }

    // 이력서제출하기
    @Test
    public void submitResume_test() throws Exception {
        // given
        SubmitResumeReqDto submitResumeReqDto = new SubmitResumeReqDto();
        submitResumeReqDto.setResumeId(1);
        submitResumeReqDto.setNoticeId(1);

        String body = om.writeValueAsString(submitResumeReqDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/company/submitResume/").content(body)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 지원공고목록
    @Test
    public void personApply_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/person/personApplyForm")
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 공고마감하기

    // 공고별구직자리스트
    @Test
    public void findNoticePerApplier_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/person/noticePerApplierForm/" + 1)
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 구직자추천리스트보기
    @Test
    public void personRecommendList_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/person/personRecommendListForm")
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 이력서목록가져오기
    @Test
    public void personResumeManage_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/person/resumeManageForm")
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 이력서삭제하기
    @Test
    public void resumeDelete_test() throws Exception {
        // given
        // LoginReqDto loginReqDto = new LoginReqDto();
        // loginReqDto.setUsername("ssar");
        // loginReqDto.setPassword("1234");
        // String body = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/person/deleteResume/1")
                        // .content(body)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON));
        System.out.println(
                "디버그 : " + resultActions.andReturn().getResponse().getContentAsString());
        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println(
                "디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 구직자 마이페이지 수정하기

    // 구직자 마이페이지
    @Test
    public void PersonMypageForm_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/personMypageForm")
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 관심구직자리스트
    @Test
    public void interestPersonSkillList_test() throws Exception {
        // given
        InterestPersonRespDto interestPersonRespDto = new InterestPersonRespDto();
        interestPersonRespDto.setPersonId(1);
        interestPersonRespDto.setMark(false);
        interestPersonRespDto.setRecommendCount(1);
        interestPersonRespDto.setPersonName("ssar");
        interestPersonRespDto.setCareer(1);
        interestPersonRespDto.setDegree("degree");
        interestPersonRespDto.setAddress("address");
        interestPersonRespDto.setPersonSkillList(null);

        String body = om.writeValueAsString(interestPersonRespDto);

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.post("/person/skillPersonMatching/personSkill").content(body)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
    }

    // 구직자 추천 페이지
    @Test
    public void PersonRecommendListFrom_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/person/recommendListForm")
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

    // 구직자 상세보기 페이지
    @Test
    public void personDetailForm_Test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(MockMvcRequestBuilders.get("/PersonDetailForm/" + 1)
                        .session(session)
                        .contentType(APPLICATION_JSON).accept(APPLICATION_JSON));

        // then
        MvcResult mvcResult = resultActions.andReturn();
        System.out.println("디버그 : " + mvcResult.getResponse().getContentAsString());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
    }

}
