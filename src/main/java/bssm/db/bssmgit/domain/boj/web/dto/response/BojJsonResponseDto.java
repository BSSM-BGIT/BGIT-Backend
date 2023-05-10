package bssm.db.bssmgit.domain.boj.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BojJsonResponseDto {

    private final String profileImageUrl;
    private final Integer solvedCount;
    private final Integer tier;
    private final Integer rating;
    private final Integer maxStreak;
    private final String bio;

}
