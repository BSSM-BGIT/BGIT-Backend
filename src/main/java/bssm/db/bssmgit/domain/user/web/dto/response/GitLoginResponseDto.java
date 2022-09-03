package bssm.db.bssmgit.domain.user.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GitLoginResponseDto {
    private String gitId;

    public GitLoginResponseDto(String gitId) {
        this.gitId = gitId;
    }
}
