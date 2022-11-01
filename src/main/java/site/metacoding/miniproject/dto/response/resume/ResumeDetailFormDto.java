package site.metacoding.miniproject.dto.response.resume;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;

@Setter
@Getter
@AllArgsConstructor
public class ResumeDetailFormDto {
	private Integer resumeId;
	private Integer personId;
	private String resumeTitle;
	private String photo;
	private String myCloud;
	private String introduction;
}
