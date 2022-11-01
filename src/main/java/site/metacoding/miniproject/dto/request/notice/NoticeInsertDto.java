package site.metacoding.miniproject.dto.request.notice;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.metacoding.miniproject.domain.need_skill.NeedSkill;
import site.metacoding.miniproject.domain.notice.Notice;

@Getter
@NoArgsConstructor
public class NoticeInsertDto {
	private Integer companyId;
	private String noticeTitle;
	private Integer career;
	private String salary;
	private String degree;
	private String noticeContent;
	private List<String> needSkill;

	public Notice toNotice() {
		return new Notice(null, this.companyId, this.noticeTitle, false, this.salary, this.degree, this.career,
				this.noticeContent, null);
	}

	public NeedSkill toNeedSkill(Integer noticeId, Integer i) {
		return new NeedSkill(null, noticeId, this.needSkill.get(i), null);
	}
}
