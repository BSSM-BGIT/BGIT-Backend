package bssm.db.bssmgit.domain.boj.web.dto.response;

import bssm.db.bssmgit.domain.boj.domain.Boj;
import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

@Data
public class BojAuthenticationResultResDto {

    private boolean result;

    public BojAuthenticationResultResDto(Boj boj) {
        if(boj.getBojId() != null) result = true;
    }
}
