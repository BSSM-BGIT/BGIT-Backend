package bssm.db.bssmgit.domain.user.domain;

import bssm.db.bssmgit.domain.post.entity.Category;
import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


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

    public void addPostCategories(Category category) {
        this.categories.add(category);
    }

    public void addPosts(Post post) {
        this.posts.add(post);
    }
}
