package site.metacoding.miniproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.domain.user.UserDao;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.dto.request.user.LoginReqDto;

@ActiveProfiles("test") // 테스트 어플리케이션 실행
@Sql("classpath:truncate.sql")
@Transactional
@AutoConfigureMockMvc // MockMvc Ioc 컨테이너에 등록 실제가 아닌 가짜
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // MOCK은 가짜 환경임
public class UserApiControllerTest {

  private static final String APPLICATION_JSON =
    "application/json; charset=utf-8";

  @Autowired
  private MockMvc mvc; // 이걸로 통신을 한다

  @Autowired
  private ObjectMapper om;

  @Autowired
  private UserDao userDao;

  private MockHttpSession session;

  @BeforeEach
  public void sessionInit() {
    session = new MockHttpSession(); // 직접 new를 했다 MockHttpSession해야 Mock가 된다
    User user = User.builder().userId(1).username("ssar").build(); // password 는 없다
    session.setAttribute("sessionUserDto", new SessionUserDto(user)); // 가짜세션이 만들어진 상태이다 -> 아직 주입은 안된 상태
  }

  @BeforeEach
  public void dataInit() {
    User user = User
      .builder()
      .userId(1)
      .username("ssar")
      .password("1234")
      .build();
    int userPS = userDao.save(user);
  }

  @Test
  public void login_test() throws Exception {
    // given
    LoginReqDto loginReqDto = new LoginReqDto();
    loginReqDto.setUsername("ssar");
    loginReqDto.setPassword("1234");
    String body = om.writeValueAsString(loginReqDto);

    // when
    ResultActions resultActions = mvc.perform(
      MockMvcRequestBuilders
        .post("/login")
        .content(body)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
    );
    System.out.println(
      "디버그 : " + resultActions.andReturn().getResponse().getContentAsString()
    );
    // then
    MvcResult mvcResult = resultActions.andReturn();
    System.out.println(
      "디버그 : " + mvcResult.getResponse().getContentAsString()
    );
    resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
  }

  @Test
  public void deleteUserId_test() throws Exception {
    // given
    // LoginReqDto loginReqDto = new LoginReqDto();
    // loginReqDto.setUsername("ssar");
    // loginReqDto.setPassword("1234");
    // String body = om.writeValueAsString(loginReqDto);

    // when
    ResultActions resultActions = mvc.perform(
      MockMvcRequestBuilders
        .delete("/deleteuser/1")
        // .content(body)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
    );
    System.out.println(
      "디버그 : " + resultActions.andReturn().getResponse().getContentAsString()
    );
    // then
    MvcResult mvcResult = resultActions.andReturn();
    System.out.println(
      "디버그 : " + mvcResult.getResponse().getContentAsString()
    );
    resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1));
  }
}
