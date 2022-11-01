package site.metacoding.miniproject.dto.response.notice;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.miniproject.domain.need_skill.NeedSkill;

@Getter
@Setter
public class NoticeApplyDto {
	private Integer noticeId;
	private Integer companyId;
	private String noticeTitle;
	private String resumeTitle;
	private String address;
	private String companyName;
	private List<NeedSkill> needSkillList;
}
