package site.metacoding.miniproject.domain.submit_resume;

import java.util.List;

public interface SubmitResumeDao {
    public void insert(SubmitResume submitResume);

    public SubmitResume findById(Integer submitResumeId);

    public List<SubmitResume> findAll();

    public void update(SubmitResume submitResume); // dto생각

    public void deleteById(Integer submitResumeId);
    
    public List<SubmitResume> findByNoticeId(Integer noticeId);
}
