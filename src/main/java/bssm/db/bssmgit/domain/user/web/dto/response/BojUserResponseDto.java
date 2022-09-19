package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

@Data
public class BojUserResponseDto {

    private final Long userId;
    private final String bojId;
    private final long solvedCount;
    private final long exp;
    private final long tier;
    private final long maxStreak;

    public BojUserResponseDto(User user) {
        this.userId = user.getId();
        this.bojId = user.getBojId();
        this.solvedCount = user.getSolvedCount();
        this.exp = user.getExp();
        this.tier = user.getTier();
        this.maxStreak = user.getMaxStreak();
    }
}
