package bssm.db.bssmgit.domain.user.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
    private String email;
}
