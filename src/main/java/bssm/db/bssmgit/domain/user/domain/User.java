package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.user.domain.type.Role;
import lombok.*;

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

    @Column(length = 8)
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

    @Builder
    public User(Long id, String password, Role role, String email, int studentGrade, int studentClassNo, int studentNo, String name, String bsmToken, String githubId) {
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
    }
}
