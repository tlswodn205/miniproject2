package site.metacoding.miniproject.domain.user;

import java.util.List;

import site.metacoding.miniproject.dto.request.company.CompanyMyPageUpdateDto;
import site.metacoding.miniproject.dto.request.person.PersonMyPageUpdateDto;

public interface UserDao {
	public void insert(User user);

	public User findById(Integer userId);

	public List<User> findAll();

	public void update(User user); // dto생각

	public void deleteById(Integer userId);

	public User findByUsername(String username);

	public void updateToUser(CompanyMyPageUpdateDto companyMyPageUpdateDto);

	public void updateToUser(PersonMyPageUpdateDto personMyPageUpdateDto);

	public User findByUserorle(String role);

}
