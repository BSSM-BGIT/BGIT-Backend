package bssm.db.bssmgit.domain.baekjoon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaekJoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String bojAuthId;

    @Column
    private String bojImg;

    @Column
    private String bojBio;

    @Column
    private String randomCode;

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
}
