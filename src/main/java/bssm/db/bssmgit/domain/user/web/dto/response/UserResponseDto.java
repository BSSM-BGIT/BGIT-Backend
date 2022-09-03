package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String email;
    private final int studentGrade;
    private final int studentClassNo;
    private final int studentNo;
    private final String name;
    private final String githubId;
    private final int commits;
    private final String bio;

    public UserResponseDto(User user){
        this.email = user.getEmail();
        this.studentGrade = user.getStudentGrade();
        this.studentClassNo = user.getStudentClassNo();
        this.studentNo = user.getStudentNo();
        this.name = user.getName();
        this.githubId = user.getGithubId();
        this.commits = user.getCommits();
        this.bio = user.getGithubMsg();
    }
}
