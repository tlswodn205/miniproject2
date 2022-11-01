package site.metacoding.miniproject.dto.response.resume;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.metacoding.miniproject.domain.person_skill.PersonSkill;

@Setter
@Getter
@AllArgsConstructor
public class ResumeFormRespDto {
    private Integer personId;
    private Integer userId;
    private String personName;
    private String personEmail;
    private String degree;
    private String address;
    private Integer career;
    private List<String> personSkillList;
}
