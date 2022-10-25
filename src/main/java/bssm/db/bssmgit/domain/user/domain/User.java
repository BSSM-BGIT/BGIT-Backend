package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.post.entity.Category;
import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.user.domain.type.Imaginary;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ImaginaryNumber> imaginaryNumbers = new ArrayList<>();

    @Column(length = 128)
    private String password;

    @Column(length = 12)
    @Enumerated(EnumType.STRING)
    private Role role;

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

    @Enumerated(EnumType.STRING)
    private Imaginary imaginary;

    @Column
    private Integer votingCount;

    public void updateStudentGrade(Integer studentGrade) {
        this.studentGrade = studentGrade;
    }

    public void updateStudentClassNo(Integer studentClassNo) {
        this.studentClassNo = studentClassNo;
    }

    public void updateStudentNo(Integer studentNo) {
        this.studentNo = studentNo;
    }

    public void updateName(String name) {
        this.name = name;
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

    public void initImaginary() {
        this.imaginary = Imaginary.REAL_NUMBER;
    }

    public void initVotingCount() {
        this.votingCount = 3;
    }

    public void updateImaginary() {
        this.imaginary = Imaginary.IMAGINARY_NUMBER;
    }

    public void addPostCategories(Category category) {
        this.categories.add(category);
    }

    public void addPosts(Post post) {
        this.posts.add(post);
    }

    public void reductionVotingCount() {
        this.votingCount--;
    }

}