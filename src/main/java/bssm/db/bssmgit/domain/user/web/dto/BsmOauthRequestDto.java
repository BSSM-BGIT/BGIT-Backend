package bssm.db.bssmgit.domain.user.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BsmOauthRequestDto {

    @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
    private String bsmAuthCode;
}
