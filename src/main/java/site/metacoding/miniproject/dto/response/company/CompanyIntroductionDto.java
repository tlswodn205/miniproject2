package site.metacoding.miniproject.dto.response.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CompanyIntroductionDto {
	private Integer companyId;
	private String photo;
	private String introduction;
	private String history;
	private String companyGoal;
	private Integer userId;
}
