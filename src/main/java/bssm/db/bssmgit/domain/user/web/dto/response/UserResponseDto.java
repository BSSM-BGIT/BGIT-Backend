package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Imaginary;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long userId;
    private String email;
    private int studentGrade;
    private int studentClassNo;
    private int studentNo;
    private final String name;
    private final String school;
    private final boolean githubAuth;
    private final boolean bojAuth;
    private boolean isImaginaryNumber;

    public UserResponseDto(User user) {
        if (user.getStudentNo() != null) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.studentGrade = user.getStudentGrade();
            this.studentClassNo = user.getStudentClassNo();
            this.studentNo = user.getStudentNo();
        }
        this.name = user.getName();
        this.school = user.getRole().name();
        this.githubAuth = user.getGithubId() != null;
        this.bojAuth = user.getBojId() != null;
        if (user.getImaginary().name().equals("IMAGINARY_NUMBER")) {
            this.isImaginaryNumber = true;
        }
    }
}
