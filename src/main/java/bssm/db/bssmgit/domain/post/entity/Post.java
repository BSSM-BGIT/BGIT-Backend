package bssm.db.bssmgit.domain.post.entity;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    private User writer;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    private int view = 1;

    @Builder
    public Post(String title, String content, User writer, int view) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.view = view;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void upView() {
        this.view += 1;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void confirmWriter(User user) {
        this.writer = user;
        user.addPosts(this);
    }
}
