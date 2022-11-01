package site.metacoding.miniproject.domain.resume;

import java.util.List;

import site.metacoding.miniproject.dto.request.resume.ResumeWriteReqDto;

public interface ResumeDao {

	public void save(ResumeWriteReqDto resumeWriteDto);

	public void insert(Resume resume);

	public Resume findById(Integer resumeId);

	public List<Resume> findAll();

	public void update(ResumeWriteReqDto resumeWriteDto); // dto생각

	public void deleteById(Integer resumeId);

	public List<Resume> findByPersonId(Integer personId);
}
