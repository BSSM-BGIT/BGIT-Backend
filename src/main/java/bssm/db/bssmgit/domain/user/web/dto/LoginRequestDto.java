package bssm.db.bssmgit.domain.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email
    private String email;

    @NotNull(message = "비밀번호를 입력해 주세요.")
    private String password;

}
