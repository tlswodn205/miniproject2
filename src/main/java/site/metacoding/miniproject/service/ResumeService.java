package site.metacoding.miniproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.miniproject.domain.resume.Resume;
import site.metacoding.miniproject.domain.resume.ResumeDao;
import site.metacoding.miniproject.dto.request.resume.ResumeWriteDto;
import site.metacoding.miniproject.dto.response.resume.ResumeDetailFormDto;

@RequiredArgsConstructor
@Service
public class ResumeService {
	private final ResumeDao resumeDao;

	@Transactional
	public void 이력서등록하기(ResumeWriteDto resumeWriteDto) {
		resumeDao.save(resumeWriteDto);
		System.out.println(resumeWriteDto.getResumeTitle());
	}

	@Transactional
	public ResumeDetailFormDto 이력서상세보기(Integer resumeId) {
		Resume resume = resumeDao.findById(resumeId);
		ResumeDetailFormDto resumeDetailFormDto = new ResumeDetailFormDto(resumeId, resume.getPersonId(),
				resume.getResumeTitle(), resume.getPhoto(), resume.getMyCloud(), resume.getIntroduction());
		return resumeDetailFormDto;
	}

	@Transactional
	public void 이력서수정하기(ResumeWriteDto resumeWriteDto) {
		resumeDao.update(resumeWriteDto);
	}
}