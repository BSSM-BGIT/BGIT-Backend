package bssm.db.bssmgit.domain.user.web.dto.response;

import lombok.Data;

@Data
public class RandomCodeResponseDto {

    private final String code;

    public RandomCodeResponseDto(String code) {
        this.code = code;
    }
}
