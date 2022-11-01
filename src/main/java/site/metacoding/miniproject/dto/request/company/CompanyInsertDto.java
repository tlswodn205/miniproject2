package site.metacoding.miniproject.dto.request.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CompanyInsertDto {
	private Integer companyId;
	private String photo;
	private String introduction;
	private String history;
	private String companyGoal;

}