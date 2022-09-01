package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String password;

    @Column(length = 12)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String email;

    @Column
    private int studentGrade;

    @Column
    private int studentClassNo;

    @Column
    private int studentNo;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String bsmToken;

    @Column(length = 64)
    private String githubId;

    @Column(length = 8)
    private int commits;

    @Column(length = 128)
    private String githubMsg;

    @Builder
    public User(Long id, String password, Role role, String email, int studentGrade, int studentClassNo, int studentNo, String name, String bsmToken, String githubId, int commits, String githubMsg) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.email = email;
        this.studentGrade = studentGrade;
        this.studentClassNo = studentClassNo;
        this.studentNo = studentNo;
        this.name = name;
        this.bsmToken = bsmToken;
        this.githubId = githubId;
        this.commits = commits;
        this.githubMsg = githubMsg;
    }

    // auth
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void matchedPassword(PasswordEncoder passwordEncoder, User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }
    }

    public void addUserAuthority() {
        this.role = Role.ROLE_USER;
    }

    public void addAdmin() {
        this.role = Role.ROLE_ADMIN;
    }

    public void updateGitId(String githubId) {
        this.githubId = githubId;
    }

    public void updateCommits(int commits) {
        this.commits = commits;
    }

    public void updateGithubMsg(String msg) {
        this.githubMsg = msg;
    }
}
