package site.metacoding.miniproject.dto.response.notice;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppliersDto {
	private Integer resumeId;
	private Integer personId;
	private String personName;
	private Integer career;
	private List<PersonSkill> personSkillList;
	private Timestamp createdAt;
}
