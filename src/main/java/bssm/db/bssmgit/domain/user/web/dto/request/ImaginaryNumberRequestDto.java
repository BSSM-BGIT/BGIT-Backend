package bssm.db.bssmgit.domain.user.web.dto.request;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImaginaryNumberRequestDto {

    private final Long reportedUserId;
    private final User user;

    @Builder
    public ImaginaryNumberRequestDto(Long reportedUserId, User user) {
        this.reportedUserId = reportedUserId;
        this.user = user;
    }

    public ImaginaryNumber toImaginaryNumber() {
        return ImaginaryNumber.builder()
                .reportedUserId(reportedUserId)
                .user(user)
                .build();
    }
}
