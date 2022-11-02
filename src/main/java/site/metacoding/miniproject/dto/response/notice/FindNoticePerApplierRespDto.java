package site.metacoding.miniproject.dto.response.notice;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.miniproject.domain.notice.Notice;
import site.metacoding.miniproject.dto.response.notice.AppliersRespDto;

@Getter
@Setter
@NoArgsConstructor
public class FindNoticePerApplierRespDto {

    private Integer noticeId;
    private Integer companyId;
    private String noticeTitle;
    private boolean isClosed;
    private String salary;
    private String degree;
    private Integer career;
    private String noticeContent;
    private List<AppliersRespDto> appliersDtoList;

    public FindNoticePerApplierRespDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.companyId = notice.getCompanyId();
        this.noticeTitle = notice.getNoticeTitle();
        this.isClosed = notice.isClosed();
        this.salary = notice.getSalary();
        this.degree = notice.getDegree();
        this.career = notice.getCareer();
        this.noticeContent = notice.getNoticeContent();
    }

}
