package site.metacoding.miniproject.dto.response.person;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import site.metacoding.miniproject.domain.person.Person;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;
import site.metacoding.miniproject.domain.resume.Resume;

@RequiredArgsConstructor
@Setter
@Getter
public class PersonRecommendListDto {

	private Integer personId;
	private Integer userId;
	private Integer recommendCount;
	private Integer subjectId;
	private String personName;
	private String resumeTitle;
	private Integer career;
	private String degree;
	private String address;
	private List<PersonSkill> skill;
	private boolean mark;
}
