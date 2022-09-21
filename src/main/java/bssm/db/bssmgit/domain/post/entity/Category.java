package bssm.db.bssmgit.domain.post.entity;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Category(String name, User user, Post post) {
        this.name = name;
        this.user = user;
        this.post = post;
    }

    public void confirmUser(User user) {
        user.addPostCategories(this);
        this.user = user;
    }

    public void confirmManagerPost(Post post) {
        post.addCategory(this);
        this.post = post;
    }
}
