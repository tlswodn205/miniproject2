package site.metacoding.miniproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.miniproject.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserDto {
    private Integer userId;
    private String username;

    public SessionUserDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
    }
}
