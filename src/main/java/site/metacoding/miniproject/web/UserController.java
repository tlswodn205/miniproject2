package site.metacoding.miniproject.web;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.dto.request.user.LoginReqDto;
import site.metacoding.miniproject.dto.response.CMRespDto;
import site.metacoding.miniproject.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/login")
    public CMRespDto<?> login(@RequestBody LoginReqDto loginDto) {

        User principal = userService.로그인(loginDto);
        if (principal == null) {
            return new CMRespDto<>(-1, "로그인실패", null);
        }
        session.setAttribute("principal", principal);

        User isLogin = (User) session.getAttribute("principal");
        return new CMRespDto<>(1, "로그인성공", isLogin);
    }

    // 로그인 페이지
    @GetMapping("/loginForm")
    public CMRespDto<?> loginForm() {
        return new CMRespDto<>(1, "로그인 페이지 불러오기 성공", null);
    }

    // 로그아웃
    @GetMapping("/logout")
    public CMRespDto<?> logout() {
        User userPS = (User) session.getAttribute("principal");
        session.invalidate();
        return new CMRespDto<>(1, "로그아웃 성공", userPS);
    }

    // 메인 페이지
    @GetMapping({ "/mainForm", "/" })
    public CMRespDto<?> mainForm() {
        return new CMRespDto<>(1, "메인 페이지 불러오기 성공", null);
    }

    // // role 구분하기 연습
    // @GetMapping("/rolesplit")
    // public
    // User principal = userService.유저롤로개인기업구분하기(null);

    @DeleteMapping("/deleteuser/{userid}")
    public CMRespDto<?> deleteUserId(@PathVariable Integer userid) {
        userService.유저삭제하기(userid);
        return new CMRespDto<>(userid, "유저삭제완료", null);
    }
}