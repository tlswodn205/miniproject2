package site.metacoding.miniproject.dto.response.person;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class PersonMyPageUpdateRespDto {
	private Integer userId;
	private String password;
	private String personName;
	private String personPhone;
	private String isGender;
	private String address;
	private String degree;
	private Integer career;
	private String personEmail;
	/* private List<PersonSkill> skill; */
}
