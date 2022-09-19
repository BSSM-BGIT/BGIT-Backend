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

    private String img;

    // solvedCount - 사용자가 푼 문제 수
    private long solvedCount;

    // exp - 사용자가 여태까지 획득한 경험치량
    private long exp;

    // tier - Bronze V를 1, Bronze IV를 2, ...,
    // Ruby I을 30, Master를 31로 표현하는 사용자 티어
    private long tier;

    // maxStreak - 최대 연속 문제 풀이일 수
    private long maxStreak;

    @Builder
    public User(String password, Role role, String email, int studentGrade, int studentClassNo, int studentNo, String name, String bsmToken, String githubId, int commits, String githubMsg, String img, long solvedCount, long exp, long tier, long maxStreak) {
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
        this.img = img;
        this.solvedCount = solvedCount;
        this.exp = exp;
        this.tier = tier;
        this.maxStreak = maxStreak;
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

    public void updateImg(String img) {
        this.img = img;
    }
}
