package site.metacoding.miniproject.dto.request.resume;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.miniproject.domain.resume.Resume;

@Getter
@Setter
public class ResumeWriteReqDto {
	private Integer personId;
	private Integer resumeId;
	private String resumeTitle;
	private String photo;
	private String introduction;
	private String address;
	private String myCloud;

	public Resume toEntity(Integer personId) {
		return Resume.builder().personId(personId).resumeTitle(this.resumeTitle).photo(this.photo)
				.introduction(this.introduction).myCloud(this.myCloud).build();
	}
}
