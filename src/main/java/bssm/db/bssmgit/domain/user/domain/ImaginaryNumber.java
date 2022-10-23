package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.post.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ImaginaryNumber extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Integer votingNumber;

    public ImaginaryNumber(Long userId) {
        this.userId = userId;
    }

    public void increaseVoting() {
        votingNumber++;
    }

    public void initVotingNumber() {
        this.votingNumber = 0;
    }
}
