package site.metacoding.miniproject.web;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.dto.response.CMRespDto;
import site.metacoding.miniproject.dto.response.resume.ResumeDetailFormRespDto;
import site.metacoding.miniproject.dto.response.resume.ResumeFormRespDto;
import site.metacoding.miniproject.service.PersonService;
import site.metacoding.miniproject.service.ResumeService;

@RequiredArgsConstructor
@RestController
public class ResumeController {

  private final HttpSession session;
  private final PersonService personService;
  private final ResumeService resumeService;

  // 이력서 등록 페이지
  @GetMapping("/s/resumeWriteForm")
  public CMRespDto<?> resumeForm() {
    SessionUserDto userPS = (SessionUserDto) session.getAttribute("principal"); // 로그인 정보 가져오기
    ResumeFormRespDto resumeFormRespDto = personService.이력서내용가져오기(userPS.getUserId()); // 이력서내용가져오기
    return new CMRespDto<>(1, "이력서 쓰기 페이지 불러오기 성공", resumeFormRespDto);
  }

  // 이력서 상세보기 페이지
  @GetMapping("/resumeDetailForm/{resumeId}")
  public CMRespDto<?> resumeDetailForm(
      Model model,
      @PathVariable Integer resumeId) {
    ResumeDetailFormRespDto personPS2 = resumeService.이력서상세보기(resumeId);
    return new CMRespDto<>(
        1,
        "이력서 상세보기 페이지 불러오기 성공",
        personPS2);
  }
}
