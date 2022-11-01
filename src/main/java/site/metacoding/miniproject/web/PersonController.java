package site.metacoding.miniproject.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.notice.Notice;
import site.metacoding.miniproject.domain.person.PersonDao;
import site.metacoding.miniproject.domain.resume.Resume;
import site.metacoding.miniproject.domain.submit_resume.SubmitResume;
import site.metacoding.miniproject.domain.subscribe.Subscribe;
import site.metacoding.miniproject.domain.subscribe.SubscribeDao;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.dto.request.person.PersonJoinDto;
import site.metacoding.miniproject.dto.request.person.PersonMyPageDto;
import site.metacoding.miniproject.dto.request.person.PersonMyPageUpdateDto;
import site.metacoding.miniproject.dto.request.resume.ResumeWriteDto;
import site.metacoding.miniproject.dto.response.CMRespDto;
import site.metacoding.miniproject.dto.response.notice.AppliersDto;
import site.metacoding.miniproject.dto.response.notice.NoticeApplyDto;
import site.metacoding.miniproject.dto.response.person.InterestPersonDto;
import site.metacoding.miniproject.dto.response.person.PersonInfoDto;
import site.metacoding.miniproject.dto.response.person.PersonRecommendListDto;
import site.metacoding.miniproject.dto.response.recommend.RecommendDetailDto;
import site.metacoding.miniproject.service.CompanyService;
import site.metacoding.miniproject.service.PersonService;
import site.metacoding.miniproject.service.UserService;

@RequiredArgsConstructor
@RestController
public class PersonController {
	private final HttpSession session;
	private final PersonService personService;
	private final UserService userService;
	private final CompanyService companyService;

	// 회원가입 응답
	@PostMapping("/person/join")
	public @ResponseBody CMRespDto<?> joinPerson(@RequestBody PersonJoinDto personJoinDto) {
		User userPS = userService.유저네임으로유저찾기(personJoinDto.getUsername());
		if (userPS != null) {
			return new CMRespDto<>(-1, "회원가입 실패", null);
		}
		personService.회원가입(personJoinDto);
		return new CMRespDto<>(1, "회원가입 성공", null);

	}

	// 개인 회원가입 페이지
	@GetMapping("/personJoinForm")
	public CMRespDto<?> perseonJoinForm(Model model) {
		return new CMRespDto<>(1, "개인 회원 가입 페이지 불러오기 성공", null);

	}

	// 이력서 응답
	@PostMapping("/save/resume/{personId}")
	public CMRespDto<?> resumeWrite(@RequestBody ResumeWriteDto resumeWriteDto,
			@PathVariable Integer personId) {
		personService.이력서등록(resumeWriteDto, personId);
		return new CMRespDto<>(1, "이력서 등록 성공", null);
	}

	@PostMapping("/person/recommend/{subjectId}")
	public @ResponseBody CMRespDto<RecommendDetailDto> companyRecommend(@PathVariable Integer subjectId) {
		User principal = (User) session.getAttribute("principal");
		RecommendDetailDto recommendDetailDto = personService.구직자추천불러오기(principal.getUserId(), subjectId);
		if (recommendDetailDto.getRecommendId() == null) {
			personService.구직자추천하기(principal.getUserId(), subjectId);
			recommendDetailDto = personService.구직자추천불러오기(principal.getUserId(), subjectId);
			return new CMRespDto<RecommendDetailDto>(1, "추천 완료", recommendDetailDto);
		}
		personService.구직자추천취소(recommendDetailDto.getRecommendId());
		recommendDetailDto = personService.구직자추천불러오기(principal.getUserId(), subjectId);
		return new CMRespDto<RecommendDetailDto>(1, "추천 취소 완료", recommendDetailDto);
	}

	// 구직자 상세보기 페이지
	@GetMapping("/PersonInfo/{personId}")
	public String 구직자상세보기(@PathVariable Integer personId, Model model) {
		User userPS = (User) session.getAttribute("principal");
		List<PersonInfoDto> personInfoDto = personService.개인정보보기(personId);
		List<PersonInfoDto> personSkillInfoDto = personService.개인기술보기(personId);
		RecommendDetailDto recommendDetailDto = new RecommendDetailDto();
		if (userPS == null) {
			recommendDetailDto = personService.구직자추천불러오기(null, personInfoDto.get(0).getUserId());
		} else {
			recommendDetailDto = personService.구직자추천불러오기(userPS.getUserId(), personInfoDto.get(0).getUserId());
		}
		model.addAttribute("recommendDetailDto", recommendDetailDto);
		model.addAttribute("personInfoDto", personInfoDto);
		model.addAttribute("personSkillInfoDto", personSkillInfoDto);
		return "person/PersonInfo";
	}

	// 구직자 추천 페이지
	@GetMapping("/person/recommendListForm")
	public CMRespDto<?> PersonRecommendList(Model model) {
		List<PersonRecommendListDto> personRecommendListDto = personService.구직자추천리스트보기();
		model.addAttribute("personRecommendListDto", personRecommendListDto);
		return new CMRespDto<>(1, "구직자 추천 리스트 페이지 불러오기 성공", null);
	}

	@PostMapping("/person/skillPersonMatching/personSkill")
	public CMRespDto<List<InterestPersonDto>> interestPersonSkillList(@RequestBody List<String> skillList,
			Model model) {
		List<InterestPersonDto> interestPersonDto = personService.관심구직자리스트(personService.기술별관심구직자찾기(skillList));
		model.addAttribute("interestPersonDto", interestPersonDto);
		return new CMRespDto<>(1, "기술별 관심 구칙자 불러오기 완료", interestPersonDto);
	}

	@PostMapping("/person/skillPersonMatching/degree")
	public CMRespDto<List<InterestPersonDto>> interestPersonDegreeList(String degree, Model model) {
		List<InterestPersonDto> interestPersonDto = personService.관심구직자리스트(personService.학력별관심구직자찾기(degree));
		model.addAttribute("interestPersonDto", interestPersonDto);
		return new CMRespDto<>(1, "학력별 관심 구칙자 불러오기 완료", interestPersonDto);
	}

	@GetMapping("/person/skillPersonMatching/{career}/career")
	public CMRespDto<List<InterestPersonDto>> interestPersonDegreeList(@PathVariable Integer career, Model model) {
		List<InterestPersonDto> interestPersonDto = personService.관심구직자리스트(personService.경력별관심구직자찾기(career));
		model.addAttribute("interestPersonDto", interestPersonDto);
		return new CMRespDto<>(1, "경력별 관심 구칙자 불러오기 완료", interestPersonDto);
	}

	@GetMapping("/person/skillPersonMatchingForm")
	public CMRespDto<?> skillPersonMatching(Model model) {
		int career = 0;
		List<InterestPersonDto> interestPersonDto = personService.관심구직자리스트(personService.경력별관심구직자찾기(career));
		model.addAttribute("interestPersonDto", interestPersonDto);
		return new CMRespDto<>(1, "기술별 구직자 매칭 페이지 불러오기 성공", null);
	}

	// 구직자 마이페이지
	@GetMapping("/personMypageForm")
	public CMRespDto<?> PersonMypage(Model model) {
		User userPS = (User) session.getAttribute("principal");
		PersonMyPageDto personMyPageDto = personService.구직자마이페이지정보보기(userPS.getUserId());
		// List<PersonMyPageDto> personMyPageSkillDto = personService.구직자기술보기();
		model.addAttribute("personMyPageDto", personMyPageDto);
		// model.addAttribute("personMyPageSkillDto",personMyPageSkillDto);
		return new CMRespDto<>(1, "구직자 마이 페이지 불러오기 성공", null);
	}

	// 구직자 마이페이지 수정하기
	@PutMapping("/api/personMypage")
	public CMRespDto<?> updateToPerson(@RequestBody PersonMyPageUpdateDto personMyPageUpdateDto) {
		System.out.println(personMyPageUpdateDto.getDegree());
		User userPS = (User) session.getAttribute("principal");
		System.out.println(userPS.getUserId());
		personService.구직자회원정보수정(userPS.getUserId(), personMyPageUpdateDto);
		return new CMRespDto<>(1, "구직자회원정보수정 성공", null);
	}

	@DeleteMapping("/person/deleteResume/{resumeId}")
	public CMRespDto<?> resumeDelete(@PathVariable Integer resumeId) {
		personService.이력서삭제하기(resumeId);
		return new CMRespDto<>(1, "이력서 삭제 성공", null);
	}

	@GetMapping("/person/resumeManageForm")
	public CMRespDto<?> personResumeManage(Model model) {
		User userPS = (User) session.getAttribute("principal");
		List<Resume> resumeList = personService.이력서목록가져오기(userPS.getUserId());
		model.addAttribute("resumeList", resumeList);
		return new CMRespDto<>(1, "이력서 관리 페이지 불러오기 성공", null);
	}

	@GetMapping("/person/personRecommendListForm")
	public CMRespDto<?> personRecommendList(Model model) {
		List<PersonRecommendListDto> personRecommendListDto = personService.구직자추천리스트보기();
		User userPS = (User) session.getAttribute("principal");
		if (userPS != null) {
			List<String> skillList = personService.유저아이디로마감임박공고의기술스택찾기(userPS.getUserId());
			if (skillList != null) {
				personRecommendListDto = personService.별찍기(personRecommendListDto, skillList);
			}
		}

		model.addAttribute("personRecommendListDto", personRecommendListDto);
		return new CMRespDto<>(1, "구직자 추천 리스트 페이지 불러오기 성공", null);
	}

	@GetMapping("/person/noticePerApplierForm/{noticeId}")
	public CMRespDto<?> findNoticePerApplier(@PathVariable Integer noticeId, Model model) {
		List<AppliersDto> appliersDtoList = personService.공고별구직자찾기(noticeId);
		Notice notice = personService.공고하나불러오기(noticeId);
		model.addAttribute("appliersDtoList", appliersDtoList);
		model.addAttribute("notice", notice);
		return new CMRespDto<>(1, "공고별 지원자 리스트 페이지 불러오기 성공", null);
	}

	@PostMapping("/company/noticeClose/{noticeId}")
	public CMRespDto<?> closeNotice(@PathVariable Integer noticeId) {
		personService.공고마감하기(noticeId);
		return new CMRespDto<>(1, "마감 완료", null);
	}

	@GetMapping("/person/personApplyForm")
	public CMRespDto<?> personApply(Model model) {
		User userPS = (User) session.getAttribute("principal");
		List<NoticeApplyDto> noticeApplyDtoList = personService.지원공고목록(userPS.getUserId());
		for (int i = 0; i < noticeApplyDtoList.size(); i++) {
			noticeApplyDtoList.get(i)
					.setNeedSkillList(companyService.noticeId로필요기술들고오기(noticeApplyDtoList.get(i).getNoticeId()));
		}
		model.addAttribute("noticeApplyDtoList", noticeApplyDtoList);
		return new CMRespDto<>(1, "공고 지원 리스트 불러오기 성공", null);
	}

	@PostMapping("/company/submitResume/")
	public CMRespDto<?> submitResume(@RequestBody SubmitResume submitResume) {
		companyService.이력서제출하기(submitResume);
		return new CMRespDto<>(1, "이력서 제출 완료", null);
	}
}