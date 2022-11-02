package site.metacoding.miniproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.company.Company;
import site.metacoding.miniproject.domain.company.CompanyDao;
import site.metacoding.miniproject.domain.need_skill.NeedSkill;
import site.metacoding.miniproject.domain.need_skill.NeedSkillDao;
import site.metacoding.miniproject.domain.notice.Notice;
import site.metacoding.miniproject.domain.notice.NoticeDao;
import site.metacoding.miniproject.domain.recommend.Recommend;
import site.metacoding.miniproject.domain.recommend.RecommendDao;
import site.metacoding.miniproject.domain.submit_resume.SubmitResume;
import site.metacoding.miniproject.domain.submit_resume.SubmitResumeDao;
import site.metacoding.miniproject.domain.subscribe.Subscribe;
import site.metacoding.miniproject.domain.subscribe.SubscribeDao;
import site.metacoding.miniproject.domain.user.User;
import site.metacoding.miniproject.domain.user.UserDao;
import site.metacoding.miniproject.dto.SessionUserDto;
import site.metacoding.miniproject.dto.request.company.CompanyInsertReqDto;
import site.metacoding.miniproject.dto.request.company.CompanyJoinReqDto;
import site.metacoding.miniproject.dto.request.company.CompanyMyPageUpdateReqDto;
import site.metacoding.miniproject.dto.request.notice.NoticeInsertReqDto;
import site.metacoding.miniproject.dto.response.company.CompanyDetailRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyIntroductionRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyJoinRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyMyPageRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyMyPageUpdateRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyRecommendRespDto;
import site.metacoding.miniproject.dto.response.notice.NoticeRespDto;
import site.metacoding.miniproject.dto.response.recommend.RecommendDetailRespDto;
import site.metacoding.miniproject.dto.response.subscribe.SubscribeRespDto;

@RequiredArgsConstructor
@Service
public class CompanyService {

	private final CompanyDao companyDao;
	private final UserDao userDao;
	private final NeedSkillDao needSkillDao;
	private final NoticeDao noticeDao;
	private final SubscribeDao subscribeDao;
	private final RecommendDao recommendDao;
	private final SubmitResumeDao submitResumeDao;

	@Transactional(rollbackFor = { RuntimeException.class })
	public CompanyJoinRespDto 기업회원가입(CompanyJoinReqDto companyJoinDto) {
		userDao.insert(companyJoinDto.toUser());
		User userPS = userDao.findByUsername(companyJoinDto.getUsername());
		companyDao.insert(companyJoinDto.toCompany(userPS.getUserId()));
		return companyDao.CompanyJoinResult(userPS.getUserId());
	}

	@Transactional
	public CompanyInsertReqDto 기업이력등록(Integer CompanyId, CompanyInsertReqDto companyInsertDto) {
		companyInsertDto.setCompanyId(CompanyId);
		companyDao.updateCompanyIntroduction(companyInsertDto);
		return companyInsertDto;
	}

	@Transactional
	public CompanyIntroductionRespDto 기업이력가져오기(Integer userId) {
		Company company = companyDao.findByUserId(userId);
		CompanyIntroductionRespDto companyIntroductionDto = new CompanyIntroductionRespDto(company);
		return companyIntroductionDto;
	}

	@Transactional
	public Company 유저아이디로찾기(Integer userId) {
		return companyDao.findByUserId(userId);
	}

	@Transactional
	public List<CompanyRecommendRespDto> 기업추천리스트보기() {
		List<CompanyRecommendRespDto> companyRecommendDtoList = companyDao.findToRecommned();

		for (int i = 0; i < companyRecommendDtoList.size(); i++) {
			List<NeedSkill> needSkillList = needSkillDao.findByNoticeId(companyRecommendDtoList.get(i).getNoticeId());
			companyRecommendDtoList.get(i).setNeedSkillList(needSkillList);
		}
		return companyRecommendDtoList;
	}

	@Transactional
	public List<Integer> 기술별공고찾기(List<String> skillList) {
		List<Notice> noticeIds = noticeDao.findAll();
		List<Integer> noticeIdList = new ArrayList<>();

		for (int i = 0; i < noticeIds.size(); i++) {
			int count = 0;
			int count2 = 0;
			for (int j = 0; j < skillList.size(); j++) {
				if (needSkillDao.findBySkillAndNoticeId(skillList.get(j), noticeIds.get(i).getNoticeId()) != null) {
					count++;
				}
			}
			if (skillList.size() == count) {
				noticeIdList.add(noticeIds.get(i).getNoticeId());
			}
		}
		return noticeIdList;
	}

	@Transactional
	public List<CompanyRecommendRespDto> NoticeId로공고불러오기(List<String> skillList) {
		List<Notice> noticeIds = noticeDao.findAll();
		List<Integer> noticeList = new ArrayList<>();

		for (int i = 0; i < noticeIds.size(); i++) {
			int count = 0;
			int count2 = 0;
			for (int j = 0; j < skillList.size(); j++) {
				if (needSkillDao.findBySkillAndNoticeId(skillList.get(j), noticeIds.get(i).getNoticeId()) != null) {
					count++;
				}
			}
			if (skillList.size() == count) {
				noticeList.add(noticeIds.get(i).getNoticeId());
			}
		}

		List<CompanyRecommendRespDto> companyRecommendDtoList = new ArrayList<>();
		for (int i = 0; i < noticeList.size(); i++) {
			CompanyRecommendRespDto companyRecommendDto = companyDao.findToNoticeId(noticeList.get(i));
			companyRecommendDto.setNeedSkillList(needSkillDao.findByNoticeId(noticeList.get(i)));
			companyRecommendDtoList.add(companyRecommendDto);
			if (i >= 19) {
				break;
			}
		}
		return companyRecommendDtoList;
	}

	@Transactional
	public List<SubscribeRespDto> 구독목록불러오기(int userId) {
		List<Subscribe> subscribeList = subscribeDao.findByUserId(userId);
		List<SubscribeRespDto> subscribeDtoList = new ArrayList<>();
		for (int i = 0; i < subscribeList.size(); i++) {
			subscribeDtoList.add(new SubscribeRespDto(subscribeList.get(i).getSubscribeId(),
					companyDao.findByUserId(subscribeList.get(i).getSubjectId()).getCompanyId(),
					companyDao.findByUserId(subscribeList.get(i).getSubjectId()).getCompanyName()));
		}
		return subscribeDtoList;
	}

	@Transactional
	public void 구독취소(Integer subscribeId) {
		subscribeDao.deleteById(subscribeId);
	}

	@Transactional
	public Company 기업한건불러오기(int companyId) {
		return companyDao.findById(companyId);
	}

	public CompanyDetailRespDto 기업상세보기불러오기(Integer companyId, SessionUserDto userPS) {
		CompanyDetailRespDto companyDetailRespDto = new CompanyDetailRespDto();
		List<NoticeRespDto> noticeRespDtoList = noticeDao.findByCompanyId(companyId);
		for (int i = 0; i < noticeRespDtoList.size(); i++) {
			noticeRespDtoList.get(i)
					.setNeedSkill((needSkillDao.findByNoticeId(noticeRespDtoList.get(i).getNoticeId())));
		}

		companyDetailRespDto.InsertCompany(companyDao.findById(companyId));
		companyDetailRespDto.setNoticeRespDtoList(noticeRespDtoList);
		if (userPS != null) {
			companyDetailRespDto.insertRecommend(
					recommendDao.findAboutsubject(userPS.getUserId(), companyDetailRespDto.getUserId()));
			companyDetailRespDto.setSubscribeId(
					subscribeDao.findByUserIdAndSubjectId(userPS.getUserId(), companyDetailRespDto.getUserId()));
		} else {
			companyDetailRespDto.insertRecommend(recommendDao.findAboutsubject(null, companyDetailRespDto.getUserId()));
		}
		return companyDetailRespDto;
	}

	@Transactional
	public List<NoticeRespDto> CompanyId로공고불러오기(Integer companyId) {
		List<NoticeRespDto> noticeRespDtoList = noticeDao.findByCompanyId(companyId);
		for (int i = 0; i < noticeRespDtoList.size(); i++) {
			noticeRespDtoList.get(i)
					.setNeedSkill((needSkillDao.findByNoticeId(noticeRespDtoList.get(i).getNoticeId())));
		}
		return noticeRespDtoList;
	}

	@Transactional
	public RecommendDetailRespDto 기업추천불러오기(Integer userId, Integer subjectId) {
		return recommendDao.findAboutsubject(userId, subjectId);
	}

	@Transactional
	public void 기업추천하기(Integer userId, Integer subjectId) {
		Recommend recommend = new Recommend(null, userId, subjectId, null);
		recommendDao.insert(recommend);
	}

	@Transactional
	public void 기업추천취소(Integer recommendId) {
		recommendDao.delete(recommendId);
	}

	@Transactional
	public Integer 구독Id불러오기(Integer userId, Integer subjectId) {
		return subscribeDao.findByUserIdAndSubjectId(userId, subjectId);
	}

	@Transactional
	public void 구독하기(Integer userId, Integer subjectId) {
		Subscribe subscribe = new Subscribe(null, userId, subjectId, null);
		subscribeDao.insert(subscribe);
	}

	@Transactional
	public void 공고등록하기(NoticeInsertReqDto noticeInsertDto) {
		System.out.println(noticeInsertDto.getDegree());
		noticeDao.insert(noticeInsertDto.toNotice());
		for (int i = 0; i < noticeInsertDto.getNeedSkill().size(); i++) {
			needSkillDao.insert(
					noticeInsertDto.toNeedSkill(noticeDao.findRecentNoticeId(noticeInsertDto.getCompanyId()), i));
		}
	}

	@Transactional
	public List<NeedSkill> noticeId로필요기술들고오기(Integer noticeId) {
		return needSkillDao.findByNoticeId(noticeId);
	}

	// 기업 마이페이지 정보 보기 id, dto(password,email ...)
	@Transactional
	public CompanyMyPageRespDto 기업마이페이지정보보기(Integer userId) {
		CompanyMyPageRespDto companyMyPageRespDtoPS = companyDao.findToCompanyMyPage(userId);
		return companyMyPageRespDtoPS;
	}

	@Transactional
	public CompanyMyPageUpdateRespDto 기업회원정보수정(CompanyMyPageUpdateReqDto companyMyPageUpdateReqDto) {
		companyDao.updateToCompany(companyMyPageUpdateReqDto);
		userDao.updateToUser(companyMyPageUpdateReqDto);
		CompanyMyPageUpdateRespDto companyMyPageUpdateRespDto = companyDao
				.CompanyMyPageUpdateResult(companyMyPageUpdateReqDto.getUserId());
		return companyMyPageUpdateRespDto;
	}

	@Transactional
	public void 이력서제출하기(SubmitResume submitResume) {
		submitResumeDao.insert(submitResume);
	}

}
