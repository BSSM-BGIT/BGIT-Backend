package bssm.db.bssmgit.domain.boj.web.dto.response;

import bssm.db.bssmgit.domain.boj.domain.Boj;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import lombok.Data;

@Data
public class BojResponseDto {

    private final String bojId;
    private final long solvedCount;
    private final long rating;
    private final long tier;
    private final long maxStreak;
    private final String bojImg;
    private final String bojBio;
    private final UserResponseDto user;

    public BojResponseDto(User user, Boj boj) {
        this.bojId = boj.getBojId();
        this.solvedCount = boj.getSolvedCount();
        this.rating = boj.getRating();
        this.tier = boj.getTier();
        this.maxStreak = boj.getMaxStreak();
        this.bojImg = boj.getBojImg();
        this.bojBio = boj.getBojBio();
        this.user = new UserResponseDto(user);
    }
}
