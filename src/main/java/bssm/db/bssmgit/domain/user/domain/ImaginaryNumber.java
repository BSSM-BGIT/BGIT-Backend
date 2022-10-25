package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.post.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ImaginaryNumber extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long reportedUserId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "do_report_user_id")
    private User user;

    @Builder
    public ImaginaryNumber(Long reportedUserId, User user) {
        this.reportedUserId = reportedUserId;
        this.user = user;
    }
}
