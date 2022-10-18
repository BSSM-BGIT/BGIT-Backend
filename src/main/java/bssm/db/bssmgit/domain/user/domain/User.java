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
    private Integer studentGrade;

    @Column
    private Integer studentClassNo;

    @Column
    private Integer studentNo;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String bsmToken;

    @Column(length = 64)
    private String githubId;

    @Column(length = 8)
    private Integer commits;

    @Column(length = 128)
    private String githubMsg;

    @Column
    private String githubImg;

    @Column
    private String bojId;
    // solvedCount - 사용자가 푼 문제 수
    @Column(length = 8)
    private Integer solvedCount;

    @Column(length = 64)
    private Integer rating;

    // tier - Bronze V를 1, Bronze IV를 2, ...,
    // Ruby I을 30, Master를 31로 표현하는 사용자 티어
    @Column(length = 4)
    private Integer tier;

    // maxStreak - 최대 연속 문제 풀이일 수
    @Column(length = 8)
    private Integer maxStreak;

    @Column
    private String bojAuthId;

    @Column
    private String bojImg;

    @Column
    private String bojBio;

    @Column
    private String randomCode;

    @Builder
    public User(List<Post> posts, List<Category> categories, String password,
                Role role, String email, Integer studentGrade,
                Integer studentClassNo, Integer studentNo, String name,
                String bsmToken, String githubId, Integer commits,
                String githubMsg, String githubImg, String bojId,
                Integer solvedCount, Integer rating, Integer tier,
                Integer maxStreak, String bojAuthId, String bojImg,
                String randomCode) {
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
        this.rating = rating;
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


    public void updateUserBojInfo(String bojId, Integer solvedCount, Integer tier, Integer rating, Integer maxStreak, String bojImg, String bio) {
        this.bojId = bojId;
        this.solvedCount = solvedCount;
        this.tier = tier;
        this.rating = Math.toIntExact(rating);
        this.maxStreak = maxStreak;
        this.bojImg = bojImg;
        this.bojBio = bio;
    }

    public void addPostCategories(Category category) {
        this.categories.add(category);
    }

    public void addPosts(Post post) {
        this.posts.add(post);
    }
}
