package site.metacoding.miniproject.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResumeDetailFormRespDto {
	private Integer resumeId;
	private Integer personId;
	private String resumeTitle;
	private String photo;
	private String myCloud;
	private String introduction;
}
