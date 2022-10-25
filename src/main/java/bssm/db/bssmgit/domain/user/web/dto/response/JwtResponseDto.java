package bssm.db.bssmgit.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponseDto {

    public String accessToken;
    public String refreshToken;

    public JwtResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
