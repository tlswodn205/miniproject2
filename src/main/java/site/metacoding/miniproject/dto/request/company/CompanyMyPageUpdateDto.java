package site.metacoding.miniproject.dto.request.company;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import site.metacoding.miniproject.domain.company.Company;
import site.metacoding.miniproject.domain.user.User;

@RequiredArgsConstructor
@Setter
@Getter
public class CompanyMyPageUpdateDto {
	private Integer userId;
	private String password;
	private String ceoName;
	private String address;
	private String companyPhone;
	private String companyEmail;
	private String tech;
}
