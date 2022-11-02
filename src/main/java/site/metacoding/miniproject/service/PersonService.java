package site.metacoding.miniproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.company.CompanyDao;
import site.metacoding.miniproject.domain.need_skill.NeedSkillDao;
import site.metacoding.miniproject.domain.notice.Notice;
import site.metacoding.miniproject.domain.notice.NoticeDao;
import site.metacoding.miniproject.domain.person.Person;
import site.metacoding.miniproject.domain.person.PersonDao;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;
import site.metacoding.miniproject.domain.person_skill.PersonSkillDao;
import site.metacoding.miniproject.domain.recommend.Recommend;
import site.metacoding.miniproject.domain.recommend.RecommendDao;
import site.metacoding.miniproject.domain.resume.Resume;
import site.metacoding.miniproject.domain.resume.ResumeDao;
import site.metacoding.miniproject.domain.submit_resume.SubmitResume;
import site.metacoding.miniproject.domain.submit_resume.SubmitResumeDao;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.domain.user.UserDao;
import site.metacoding.miniproject.dto.request.person.PersonJoinReqDto;
import site.metacoding.miniproject.dto.request.person.PersonMyPageReqDto;
import site.metacoding.miniproject.dto.request.person.PersonMyPageUpdateReqDto;
import site.metacoding.miniproject.dto.request.resume.ResumeWriteReqDto;
import site.metacoding.miniproject.dto.response.notice.AppliersRespDto;
import site.metacoding.miniproject.dto.response.notice.FindNoticePerApplierRespDto;
import site.metacoding.miniproject.dto.response.notice.NoticeApplyRespDto;
import site.metacoding.miniproject.dto.response.person.InterestPersonRespDto;
import site.metacoding.miniproject.dto.response.person.PersonInfoRespDto;
import site.metacoding.miniproject.dto.response.person.PersonJoinRespDto;
import site.metacoding.miniproject.dto.response.person.PersonRecommendListRespDto;
import site.metacoding.miniproject.dto.response.recommend.RecommendDetailRespDto;
import site.metacoding.miniproject.dto.response.resume.ResumeFormRespDto;

@RequiredArgsConstructor
@Service
public class PersonService {

	private final PersonDao personDao;
	private final UserDao userDao;
	private final PersonSkillDao personSkillDao;
	private final ResumeDao resumeDao;
	private final RecommendDao recommendDao;
	private final SubmitResumeDao submitResumeDao;
	private final NoticeDao noticeDao;
	private final NeedSkillDao needSkillDao;

	@Transactional(rollbackFor = { RuntimeException.class })
	public PersonJoinRespDto 회원가입(PersonJoinReqDto personJoinReqDto) {
		userDao.insert(personJoinReqDto.toUser());
		User userPS = userDao.findByUsername(personJoinReqDto.getUsername());
		personDao.insert(personJoinReqDto.toPerson(userPS.getUserId()));
		Integer personId = personDao.findToId(userPS.getUserId());
		List<String> personSkillList = personJoinReqDto.getPersonSkillList();

		for (int i = 0; i < personSkillList.size(); i++) {
			personSkillDao.insert(personId, personSkillList.get(i));
		}
		PersonJoinRespDto personJoinRespDto = new PersonJoinRespDto();
		personJoinRespDto.setPersonSkillList(personSkillDao.findByPersonId(personId));
		return personJoinRespDto;
	}

	@Transactional
	public ResumeFormRespDto 이력서내용가져오기(Integer personId) {
		Person person = personDao.findById(personId);
		ResumeFormRespDto resumeFormDto = new ResumeFormRespDto(person, personSkillDao.findByPersonId(personId));
		return resumeFormDto;
	}

	@Transactional
	public List<Integer> 기술별관심구직자찾기(List<String> skillList) {

		List<Integer> interesPersonIdList = new ArrayList<Integer>();

		List<Person> personList = personDao.findAll();

		for (int i = 0; i < personList.size(); i++) {
			int count = 0;
			int personId = personList.get(i).getPersonId();
			for (int j = 0; j < skillList.size(); j++) {
				if (personSkillDao.findBySkillAndPersonId(skillList.get(j), personId) == null) {
					continue;
				}
				count++;
			}
			if (count >= skillList.size()) {
				interesPersonIdList.add(personId);
			}
		}

		return interesPersonIdList;
	}

	@Transactional
	public List<Integer> 학력별관심구직자찾기(String degree) {
		List<Integer> personIdList = personDao.findByDegree(degree);
		return personIdList;
	}

	@Transactional
	public List<Integer> 경력별관심구직자찾기(Integer career) {
		List<Integer> personIdList = personDao.findByCareer(career);
		return personIdList;
	}

	@Transactional
	public List<InterestPersonRespDto> 관심구직자리스트(List<String> skillList) {
		List<Integer> interesPersonIdList = new ArrayList<Integer>();

		List<Person> personList = personDao.findAll();

		for (int i = 0; i < personList.size(); i++) {
			int count = 0;
			int personId = personList.get(i).getPersonId();
			for (int j = 0; j < skillList.size(); j++) {
				if (personSkillDao.findBySkillAndPersonId(skillList.get(j), personId) == null) {
					continue;
				}
				count++;
			}
			if (count >= skillList.size()) {
				interesPersonIdList.add(personId);
			}
		}

		List<InterestPersonRespDto> interestPersonDtoList = new ArrayList<InterestPersonRespDto>();
		int count = 0;

		for (int i = 0; i < interesPersonIdList.size(); i++) {
			count++;
			Person person = personDao.findById(interesPersonIdList.get(i));
			RecommendDetailRespDto CompanyDetailRecomDto = recommendDao.findAboutsubject(null, person.getUserId());
			InterestPersonRespDto interestPersonDto = new InterestPersonRespDto(person.getPersonId(), false,
					CompanyDetailRecomDto.getRecommendCount(), person.getPersonName(), person.getCareer(),
					person.getDegree(), person.getAddress(), personSkillDao.findByPersonId(interesPersonIdList.get(i)));

			interestPersonDtoList.add(interestPersonDto);
			if (count >= 20) {
				break;
			}
		}

		return interestPersonDtoList;
	}

	@Transactional
	public void 이력서등록(ResumeWriteReqDto resumeWriteDto, Integer personId) {
		Resume resume = resumeWriteDto.toEntity(personId);
		resumeDao.insert(resume);
	}

	@Transactional
	public Integer 개인번호갖고오기(Integer userId) {
		return personDao.findToId(userId);
	}

	@Transactional
	public List<PersonInfoRespDto> 개인정보보기(Integer personId) {
		return personDao.personInfo(personId);
	}

	@Transactional
	public List<PersonInfoRespDto> 개인기술보기(Integer personId) {
		return personSkillDao.personSkillInfo(personId);
	}

	@Transactional
	public List<PersonRecommendListRespDto> 구직자추천리스트보기() {
		List<PersonRecommendListRespDto> personRecommendListDto = personDao.findToPersonRecommned();
		for (int i = 0; i < personRecommendListDto.size(); i++) {
			Integer personId = personRecommendListDto.get(i).getPersonId();
			personRecommendListDto.get(i).setSkill(personSkillDao.findByPersonId(personId));
		}
		return personRecommendListDto;
	}

	// 구직자 마이페이지 정보 보기
	@Transactional
	public PersonMyPageReqDto 구직자마이페이지정보보기(Integer userId) {
		PersonMyPageReqDto personMyPageDtoPs = personDao.findToPersonMyPage(userId);
		return personMyPageDtoPs;

	}

	// 구직자 마이페이지 정보 수정
	@Transactional
	public void 구직자회원정보수정(PersonMyPageUpdateReqDto personMyPageUpdateDto) {
		personDao.updateToPerson(personMyPageUpdateDto);
		userDao.updateToUser(personMyPageUpdateDto);
	}

	@Transactional
	public List<Resume> 이력서목록가져오기(Integer userId) {
		List<Resume> resumeList = resumeDao.findByPersonId(personDao.findToId(userId));
		if (resumeList.size() == 0) {
			return null;
		}
		return resumeList;
	}

	@Transactional
	public void 이력서삭제하기(Integer resumeId) {
		resumeDao.deleteById(resumeId);
	}

	@Transactional
	public List<PersonRecommendListRespDto> 추천구직차불러오기() {
		List<PersonRecommendListRespDto> PersonRecommendDtoList = personDao.findToPersonRecommned();
		for (int i = 0; i < PersonRecommendDtoList.size(); i++) {
			PersonRecommendDtoList.get(i)
					.setSkill(personSkillDao.findByPersonId(PersonRecommendDtoList.get(i).getPersonId()));
		}
		return PersonRecommendDtoList;
	}

	@Transactional
	public RecommendDetailRespDto 구직자추천불러오기(Integer userId, Integer subjectId) {
		return recommendDao.findAboutsubject(userId, subjectId);
	}

	@Transactional
	public void 구직자추천하기(Integer userId, Integer subjectId) {
		Recommend recommend = new Recommend(null, userId, subjectId, null);
		recommendDao.insert(recommend);
	}

	@Transactional
	public void 구직자추천취소(Integer recommendId) {
		recommendDao.delete(recommendId);
	}

	@Transactional
	public List<AppliersRespDto> 공고별구직자찾기(Integer noticeId) {
		List<SubmitResume> submitResumedList = submitResumeDao.findByNoticeId(noticeId);
		List<AppliersRespDto> appliersDtoList = new ArrayList<>();
		for (int i = 0; i < submitResumedList.size(); i++) {
			Integer personId = resumeDao.findById(submitResumedList.get(i).getResumeId()).getPersonId();
			Person person = personDao.findById(personId);
			personDao.findById(personId);
			List<String> personSkillList = personSkillDao.findByPersonId(personId);
			appliersDtoList
					.add(new AppliersRespDto(submitResumedList.get(i).getResumeId(), personId, person.getPersonName(),
							person.getCareer(), personSkillList, submitResumedList.get(i).getCreatedAt()));
		}
		return appliersDtoList;
	}

	@Transactional
	public Notice 공고하나불러오기(int noticeId) {
		return noticeDao.findById(noticeId);
	}

	@Transactional
	public FindNoticePerApplierRespDto 공고별구직자리스트(Integer noticeId) {
		FindNoticePerApplierRespDto findNoticePerApplierRespDto = new FindNoticePerApplierRespDto(
				noticeDao.findById(noticeId));
		List<SubmitResume> submitResumedList = submitResumeDao.findByNoticeId(noticeId);
		List<AppliersRespDto> appliersDtoList = new ArrayList<>();
		for (int i = 0; i < submitResumedList.size(); i++) {
			Integer personId = resumeDao.findById(submitResumedList.get(i).getResumeId()).getPersonId();
			Person person = personDao.findById(personId);
			personDao.findById(personId);
			List<String> personSkillList = personSkillDao.findByPersonId(personId);
			appliersDtoList
					.add(new AppliersRespDto(submitResumedList.get(i).getResumeId(), personId, person.getPersonName(),
							person.getCareer(), personSkillList, submitResumedList.get(i).getCreatedAt()));
		}
		findNoticePerApplierRespDto.setAppliersDtoList(appliersDtoList);
		return findNoticePerApplierRespDto;
	}

	@Transactional
	public void 공고마감하기(Integer noticeId) {
		noticeDao.closeNotice(noticeId, true);

	}

	@Transactional
	public List<NoticeApplyRespDto> 지원공고목록(Integer userId) {
		return noticeDao.findNoticeApply(userId);
	}

	@Transactional
	public List<String> 유저아이디로마감임박공고의기술스택찾기(Integer userId) {
		List<String> skillList = needSkillDao.findByUserId(userId);
		System.out.println(skillList.get(0));
		return skillList;
	}

	@Transactional
	public List<PersonRecommendListRespDto> 별찍기(List<PersonRecommendListRespDto> personIdList, List<String> skillList) {

		for (int i = 0; i < personIdList.size(); i++) {
			int count = 0;
			List<String> personSkillList = personSkillDao.findByPersonId(personIdList.get(i).getPersonId());
			for (int j = 0; j < personSkillList.size(); j++) {
				for (int j2 = 0; j2 < skillList.size(); j2++) {
					if (personSkillList.get(j).equals(skillList.get(j2))) {
						count++;
					}
				}
			}
			System.out.print(skillList.size() * 0.7999999 + "\t");
			System.out.println(count);
			if (skillList.size() * 0.7999999999 <= count) {
				personIdList.get(i).setMark(true);
			}
			System.out.println(personIdList.get(i).isMark());
			System.out.println("============================");
		}
		return personIdList;
	}

}
