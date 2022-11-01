package site.metacoding.miniproject.domain.notice;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import site.metacoding.miniproject.dto.response.notice.NoticeApplyDto;
import site.metacoding.miniproject.dto.response.notice.NoticeRespDto;

public interface NoticeDao {
	public void insert(Notice notice);

	public Notice findById(Integer noticeId);

	public List<Notice> findAll();

	public void update(Notice notice); // dto생각

	public void deleteById(Integer noticeId);

	public List<NoticeRespDto> findByCompanyId(Integer companyId);

	public Integer findRecentNoticeId(Integer companyId);

	public void closeNotice(@Param("noticeId") Integer noticeId, @Param("isClosed") boolean isClosed);

	public List<NoticeApplyDto> findNoticeApply(Integer userId);
}
