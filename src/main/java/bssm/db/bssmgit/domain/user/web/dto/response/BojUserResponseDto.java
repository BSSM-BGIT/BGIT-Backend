package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class BojUserResponseDto {

    private final String bojId;
    private final long solvedCount;
    private final long exp;
    private final long tier;
    private final long maxStreak;
    private final UserResponseDto user;

    public BojUserResponseDto(User user) {
        this.bojId = user.getBojId();
        this.solvedCount = user.getSolvedCount();
        this.exp = user.getExp();
        this.tier = user.getTier();
        this.maxStreak = user.getMaxStreak();
        UserResponseDto userResponseDto = new UserResponseDto(user);
        this.user = userResponseDto;
    }
}
