package site.metacoding.miniproject.dto.response.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeDto {
	private Integer subscribeId;
	private Integer companyId;
	private String companyName;
}
