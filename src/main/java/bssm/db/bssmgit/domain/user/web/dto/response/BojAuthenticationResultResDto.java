package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

@Data
public class BojAuthenticationResultResDto {

    private boolean result;

    public BojAuthenticationResultResDto(User user) {
        if(user.getBojId() != null) result = true;
    }
}
