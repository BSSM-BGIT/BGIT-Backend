package bssm.db.bssmgit.domain.github.domain;

import bssm.db.bssmgit.domain.github.domain.type.Imaginary;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitHub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String githubId;

    @Column(length = 8)
    private Integer commits;

    @Column(length = 1024)
    private String githubMsg;

    @Column
    private String githubImg;


    @Enumerated(EnumType.STRING)
    private Imaginary imaginary;

    @Column
    private Integer votingCount;

    public void updateGitId(String githubId) {
        this.githubId = githubId;
    }

    public void updateGitInfo(int commits, String bio, String githubImg) {
        this.commits = commits;
        this.githubMsg = bio;
        this.githubImg = githubImg;
    }

    public boolean hasNotGithubId() {
        return this.githubId == null;
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

    public void reductionVotingCount() {
        this.votingCount--;
    }
}
