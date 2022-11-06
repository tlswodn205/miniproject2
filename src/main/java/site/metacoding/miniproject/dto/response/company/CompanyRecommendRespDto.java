package site.metacoding.miniproject.dto.response.company;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CompanyRecommendRespDto {
	private Integer recommendCount;
	private Integer noticeId;
	private Integer userId;
	private Integer companyId;
	private String noticeTitle;
	private String companyName;
	private String address;
	private Integer career;
	private String degree;
	private String salary;
	private Timestamp createdAt;
	private List<String> needSkillList;
}
