package site.metacoding.miniproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.company.Company;
import site.metacoding.miniproject.domain.company.CompanyDao;
import site.metacoding.miniproject.domain.need_skill.NeedSkillDao;
import site.metacoding.miniproject.domain.notice.Notice;
import site.metacoding.miniproject.domain.notice.NoticeDao;
import site.metacoding.miniproject.domain.recommend.Recommend;
import site.metacoding.miniproject.domain.recommend.RecommendDao;
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
import site.metacoding.miniproject.dto.request.resume.SubmitResumeReqDto;
import site.metacoding.miniproject.dto.response.company.CompanyDetailRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyInsertRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyIntroductionRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyJoinRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyMyPageRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyMyPageUpdateRespDto;
import site.metacoding.miniproject.dto.response.company.CompanyRecommendRespDto;
import site.metacoding.miniproject.dto.response.notice.NoticeInsertRespDto;
import site.metacoding.miniproject.dto.response.notice.NoticeRespDto;
import site.metacoding.miniproject.dto.response.recommend.RecommendDetailRespDto;
import site.metacoding.miniproject.dto.response.resume.SubmitResumeRespDto;
import site.metacoding.miniproject.dto.response.subscribe.SubscribeDeleteRespDto;
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
  public CompanyJoinRespDto ??????????????????(CompanyJoinReqDto companyJoinDto) {
    userDao.insert(companyJoinDto.toUser());
    User userPS = userDao.findByUsername(companyJoinDto.getUsername());
    companyDao.insert(companyJoinDto.toCompany(userPS.getUserId()));
    CompanyJoinRespDto companyJoinRespDto = companyDao.CompanyJoinResult(userPS.getUserId());
    return companyJoinRespDto;
  }

  @Transactional
  public CompanyInsertReqDto ??????????????????(Integer CompanyId, CompanyInsertReqDto companyInsertReqDto) {
    companyInsertReqDto.setCompanyId(CompanyId);
    companyDao.updateCompanyIntroduction(companyInsertReqDto);
    return companyInsertReqDto;
  }

  @Transactional
  public CompanyInsertRespDto ????????????????????????(Integer CompanyId, CompanyInsertReqDto companyInsertReqDto) {
    companyInsertReqDto.setCompanyId(CompanyId);
    CompanyInsertRespDto companyInsertRespDto = companyDao.companyInfoUpdateResult(CompanyId);
    return companyInsertRespDto;
  }

  @Transactional
  public CompanyIntroductionRespDto ????????????????????????(Integer userId) {
    Company company = companyDao.findByUserId(userId);
    CompanyIntroductionRespDto companyIntroductionDto = new CompanyIntroductionRespDto(company);
    return companyIntroductionDto;
  }

  @Transactional
  public List<NoticeRespDto> ????????????????????????????????????(Integer userId) {
    Company company = companyDao.findByUserId(userId);
    List<NoticeRespDto> noticeRespDtoList = noticeDao.findByCompanyId(company.getCompanyId());
    for (int i = 0; i < noticeRespDtoList.size(); i++) {
      noticeRespDtoList.get(i)
          .setNeedSkill((needSkillDao.findByNoticeId(noticeRespDtoList.get(i).getNoticeId())));
    }
    return noticeRespDtoList;
  }

  @Transactional
  public Company ????????????????????????(Integer userId) {
    return companyDao.findByUserId(userId);
  }

  @Transactional
  public List<CompanyRecommendRespDto> ???????????????????????????() {
    List<CompanyRecommendRespDto> companyRecommendDtoList = companyDao.findToRecommned();

    for (int i = 0; i < companyRecommendDtoList.size(); i++) {
      List<String> needSkillList = needSkillDao.findByNoticeId(companyRecommendDtoList.get(i).getNoticeId());
      companyRecommendDtoList.get(i).setNeedSkillList(needSkillList);
    }
    return companyRecommendDtoList;
  }

  @Transactional
  public List<Integer> ?????????????????????(List<String> skillList) {
    List<Notice> noticeIds = noticeDao.findAll();
    List<Integer> noticeIdList = new ArrayList<>();

    for (int i = 0; i < noticeIds.size(); i++) {
      int count = 0;
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
  public List<CompanyRecommendRespDto> NoticeId?????????????????????(List<String> skillList) {
    List<Notice> noticeIds = noticeDao.findAll();
    List<Integer> noticeList = new ArrayList<>();

    for (int i = 0; i < noticeIds.size(); i++) {
      int count = 0;
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
  public List<SubscribeRespDto> ????????????????????????(int userId) {
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
  public SubscribeDeleteRespDto ????????????(Integer userId, Integer subjectId) {
    SubscribeDeleteRespDto subscribeDeleteRespDto = subscribeDao.SubscribeDeleteResult(userId,
    subjectId);
    subscribeDao.deleteById(userId, subjectId);
    return subscribeDeleteRespDto;
  }

  @Transactional
  public Company ????????????????????????(int companyId) {
    return companyDao.findById(companyId);
  }

  public CompanyDetailRespDto ??????????????????????????????(
      Integer companyId,
      SessionUserDto userPS) {
    CompanyDetailRespDto companyDetailRespDto = new CompanyDetailRespDto();
    List<NoticeRespDto> noticeRespDtoList = noticeDao.findByCompanyId(
        companyId);
    for (int i = 0; i < noticeRespDtoList.size(); i++) {
      noticeRespDtoList
          .get(i)
          .setNeedSkill(
              (needSkillDao.findByNoticeId(noticeRespDtoList.get(i).getNoticeId())));
    }

    companyDetailRespDto.InsertCompany(companyDao.findById(companyId));
    companyDetailRespDto.setNoticeRespDtoList(noticeRespDtoList);
    if (userPS != null) {
      companyDetailRespDto.insertRecommend(
          recommendDao.findAboutsubject(
              userPS.getUserId(),
              companyDetailRespDto.getUserId()));
      companyDetailRespDto.setSubscribeId(
          subscribeDao.findByUserIdAndSubjectId(
              userPS.getUserId(),
              companyDetailRespDto.getUserId()));
    } else {
      companyDetailRespDto.insertRecommend(
          recommendDao.findAboutsubject(null, companyDetailRespDto.getUserId()));
    }
    return companyDetailRespDto;
  }

  @Transactional
  public List<NoticeRespDto> CompanyId?????????????????????(Integer companyId) {
    List<NoticeRespDto> noticeRespDtoList = noticeDao.findByCompanyId(
        companyId);
    for (int i = 0; i < noticeRespDtoList.size(); i++) {
      noticeRespDtoList
          .get(i)
          .setNeedSkill(
              (needSkillDao.findByNoticeId(noticeRespDtoList.get(i).getNoticeId())));
    }
    return noticeRespDtoList;
  }

  @Transactional
  public RecommendDetailRespDto ????????????????????????(
      Integer userId,
      Integer subjectId) {
    return recommendDao.findAboutsubject(userId, subjectId);
  }

  @Transactional
  public void ??????????????????(Integer userId, Integer subjectId) {
    Recommend recommend = new Recommend(null, userId, subjectId, null);
    recommendDao.insert(recommend);
  }

  @Transactional
  public void ??????????????????(Integer recommendId) {
    recommendDao.delete(recommendId);
  }

  @Transactional
  public Integer ??????Id????????????(Integer userId, Integer subjectId) {
    return subscribeDao.findByUserIdAndSubjectId(userId, subjectId);
  }

  @Transactional
  public void ????????????(Integer userId, Integer subjectId) {
    Subscribe subscribe = new Subscribe(null, userId, subjectId, null);
    subscribeDao.insert(subscribe);
  }

  @Transactional
  public NoticeInsertRespDto ??????????????????(NoticeInsertReqDto noticeInsertDto, SessionUserDto userPS) {
    noticeInsertDto.setCompanyId(companyDao.findByUserId(userPS.getUserId()).getCompanyId());
    noticeDao.insert(noticeInsertDto.toNotice());
    for (int i = 0; i < noticeInsertDto.getNeedSkill().size(); i++) {
      needSkillDao.insert(
          noticeInsertDto.toNeedSkill(
              noticeDao.findRecentNoticeId(noticeInsertDto.getCompanyId()),
              i));
    }
    
    NoticeInsertRespDto noticeInsertRespDto = noticeDao.noticeInsertResult(noticeInsertDto.getCompanyId());
    noticeInsertRespDto.setNeedSkill(needSkillDao.findByNoticeId(noticeInsertRespDto.getNoticeId()));
    return noticeInsertRespDto;
  }

  @Transactional
  public List<String> noticeId???????????????????????????(Integer noticeId) {
    return needSkillDao.findByNoticeId(noticeId);
  }

  // ?????? ??????????????? ?????? ?????? id, dto(password,email ...)
  @Transactional
  public CompanyMyPageRespDto ?????????????????????????????????(Integer userId) {
    CompanyMyPageRespDto companyMyPageRespDtoPS = companyDao.findToCompanyMyPage(userId);
    return companyMyPageRespDtoPS;
  }

  @Transactional
  public CompanyMyPageUpdateRespDto ????????????????????????(
      CompanyMyPageUpdateReqDto companyMyPageUpdateReqDto) {
    companyDao.updateToCompany(companyMyPageUpdateReqDto);
    userDao.updateToUser(companyMyPageUpdateReqDto);
    CompanyMyPageUpdateRespDto companyMyPageUpdateRespDto = companyDao.CompanyMyPageUpdateResult(
        companyMyPageUpdateReqDto.getUserId());
    return companyMyPageUpdateRespDto;
  }

  @Transactional
  public SubmitResumeRespDto ?????????????????????(SubmitResumeReqDto submitResumeReqDto) {
    submitResumeDao.insert(submitResumeReqDto);
    SubmitResumeRespDto submitResumeRespDto = submitResumeDao.submitResumeResult(submitResumeReqDto);
    return submitResumeRespDto;
  }
}
