package site.metacoding.miniproject.dto.response.person;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;

@RequiredArgsConstructor
@Setter
@Getter
public class PersonMyPageRespDto {
	private Integer userId;
	private String username;
	private String password;
	private Integer personId;
	private String personName;
	private String personPhone;
	private String isGender;
	private String address;
	private String degree;
	private String career;
	private String personEmail;
	private List<PersonSkill> skill;
}