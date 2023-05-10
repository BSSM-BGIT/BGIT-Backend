package bssm.db.bssmgit.domain.boj.web.dto.response;

import lombok.Data;

@Data
public class RandomCodeResponseDto {

    private final String code;

    public RandomCodeResponseDto(String code) {
        this.code = code;
    }
}
