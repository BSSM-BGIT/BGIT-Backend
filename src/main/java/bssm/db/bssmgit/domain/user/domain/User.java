package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.post.entity.Category;
import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<>();

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

    private String githubImg;

    private String bojId;
    // solvedCount - 사용자가 푼 문제 수
    @Column(length = 8)
    private long solvedCount;

    // exp - 사용자가 여태까지 획득한 경험치량
    @Column(length = 64)
    private long exp;

    // tier - Bronze V를 1, Bronze IV를 2, ...,
    // Ruby I을 30, Master를 31로 표현하는 사용자 티어
    @Column(length = 4)
    private long tier;

    // maxStreak - 최대 연속 문제 풀이일 수
    @Column(length = 8)
    private long maxStreak;

    private String bojAuthId;
    private String bojImg;
    private String randomCode;

    @Builder
    public User(List<Post> posts, List<Category> categories, String password, Role role, String email, int studentGrade, int studentClassNo, int studentNo, String name, String bsmToken, String githubId, int commits, String githubMsg, String githubImg, String bojId, long solvedCount, long exp, long tier, long maxStreak, String bojAuthId, String bojImg, String randomCode) {
        this.posts = posts;
        this.categories = categories;
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
        this.githubImg = githubImg;
        this.bojId = bojId;
        this.solvedCount = solvedCount;
        this.exp = exp;
        this.tier = tier;
        this.maxStreak = maxStreak;
        this.bojAuthId = bojAuthId;
        this.bojImg = bojImg;
        this.randomCode = randomCode;
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

    public void updateGitId(String githubId) {
        this.githubId = githubId;
    }

    public void updateGitInfo(int commits, String bio, String githubImg) {
        this.commits = commits;
        this.githubMsg = bio;
        this.githubImg = githubImg;
    }

    public void updateBojAuthId(String bojAuthId) {
        this.bojAuthId = bojAuthId;
    }

    public void updateRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }


    public void updateUserBojInfo(Long solvedCount, Long tier, Long exp, Long maxStreak, String bojImg) {
        this.solvedCount = solvedCount;
        this.tier = tier;
        this.exp = exp;
        this.maxStreak = maxStreak;
        this.bojImg = bojImg;
    }

    public void updateBojId(String bojId) {
        this.bojId = bojId;
    }

    public void addPostCategories(Category category) {
        this.categories.add(category);
    }

    public void addPosts(Post post) {
        this.posts.add(post);
    }
}
